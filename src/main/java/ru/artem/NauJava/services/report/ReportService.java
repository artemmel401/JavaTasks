package ru.artem.NauJava.services.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.artem.NauJava.entity.Report;
import ru.artem.NauJava.entity.Seat;
import ru.artem.NauJava.model.Status;
import ru.artem.NauJava.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artem.NauJava.repository.SeatRepository;
import ru.artem.NauJava.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    private final UserRepository userRepository;

    private final SeatRepository seatRepository;

    private static final Logger log = LoggerFactory.getLogger(ReportService.class);

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Autowired
    public ReportService(ReportRepository reportRepository,
                         UserRepository userRepository,
                         SeatRepository seatRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.seatRepository = seatRepository;
    }

    public String getReportContent(Long reportId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Отчет с ID " + reportId + " не найден"));
        switch (report.getStatus()) {
            case ERROR -> {
                return "Ошибка при генерации отчета";
            }
            case CREATED -> {
                return "Отчет формируется";
            }
            case FINISHED -> {
                return report.getContent();
            }
        }
        return "";
    }

    public Long createReport() {
        Report report = new Report();
        report.setStatus(Status.CREATED);
        report.setContent("");
        Report savedReport = reportRepository.save(report);
        return savedReport.getId();
    }

    public void generateReportAsync(Long reportId) {
        CompletableFuture.runAsync(() -> {
            long startTime = System.currentTimeMillis();

            try {
                Report report = reportRepository.findById(reportId)
                        .orElseThrow(() -> new RuntimeException("Отчет не найден"));

                CompletableFuture<CalculationResult<Integer>> userCountFuture = createUserCountTask();
                CompletableFuture<CalculationResult<List<Seat>>> objectListFuture = createObjectListTask();

                CompletableFuture.allOf(userCountFuture, objectListFuture).join();

                CalculationResult<Integer> userCountResult = userCountFuture.join();
                CalculationResult<List<Seat>> objectListResult = objectListFuture.join();

                long totalElapsed = (System.currentTimeMillis() - startTime);

                String reportContent = generateHtmlReport(
                        userCountResult,
                        objectListResult,
                        totalElapsed
                );

                report.setContent(reportContent);
                report.setStatus(Status.FINISHED);
                reportRepository.save(report);

            } catch (Exception e) {
                log.error("Ошибка при формировании отчета ID: {}", reportId, e);
                Report report = reportRepository.findById(reportId).orElse(null);
                if (report != null) {
                    report.setStatus(Status.ERROR);
                    reportRepository.save(report);
                }
                throw new RuntimeException("Ошибка при формировании отчета", e);
            }
        }, executorService);
    }

    private record CalculationResult<T>(T result, long executionTime) {
    }

    private CompletableFuture<CalculationResult<Integer>> createUserCountTask() {
        CompletableFuture<CalculationResult<Integer>> future = new CompletableFuture<>();

        Thread thread = new Thread(() -> {
            long taskStartTime = System.currentTimeMillis();
            try {
                // Имитация длительной операции
                Thread.sleep(20000);
                Integer result = calculateUserCount();
                long taskElapsed = (System.currentTimeMillis() - taskStartTime);
                future.complete(new CalculationResult<>(result, taskElapsed));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                future.completeExceptionally(new RuntimeException("Ошибка при подсчете пользователей", e));
            } catch (Exception e) {
                future.completeExceptionally(new RuntimeException("Ошибка при подсчете пользователей", e));
            }
        });

        thread.start();
        return future;
    }

    private CompletableFuture<CalculationResult<List<Seat>>> createObjectListTask() {
        CompletableFuture<CalculationResult<List<Seat>>> future = new CompletableFuture<>();

        Thread thread = new Thread(() -> {
            long taskStartTime = System.currentTimeMillis();
            try {
                // Имитация длительной операции
                Thread.sleep(15000);
                List<Seat> result = getObjectList();
                long taskElapsed = (System.currentTimeMillis() - taskStartTime);
                future.complete(new CalculationResult<>(result, taskElapsed));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                future.completeExceptionally(new RuntimeException("Ошибка при получении списка объектов", e));
            } catch (Exception e) {
                future.completeExceptionally(new RuntimeException("Ошибка при получении списка объектов", e));
            }
        });

        thread.start();
        return future;
    }

    private String generateHtmlReport(CalculationResult<Integer> userCountResult,
                                      CalculationResult<List<Seat>> objectListResult,
                                      long totalElapsed) {

        String objectListHtml = objectListResult.result().stream()
                .map(seat -> String.format(
                        "<tr>" +
                                "<td>%d</td>" +
                                "<td>%s</td>" +
                                "<td>%s</td>" +
                                "<td>%s</td>" +
                                "</tr>",
                        seat.getId(),
                        escapeHtml(seat.getRowNumber() != null ? seat.getRowNumber().toString() : "N/A"),
                        escapeHtml(seat.getSeatNumber() != null ? seat.getSeatNumber().toString() : "N/A"),
                        seat.isFree() ? "Доступно" : "Занято"
                ))
                .collect(Collectors.joining("\n"));

        return String.format(
                "<div class='header'>" +
                        "<h1>Отчет статистики приложения</h1>" +
                        "<p>Статистика сформирована: %s</p>" +
                        "</div>" +
                        "<div class='section'>" +
                        "<h2>Основная статистика</h2>" +
                        "<table class='stats-table'>" +
                        "<tr><th>Показатель</th><th>Значение</th></tr>" +
                        "<tr><td>Количество зарегистрированных пользователей</td><td>%d</td></tr>" +
                        "<tr><td>Количество объектов (мест)</td><td>%d</td></tr>" +
                        "</table>" +
                        "</div>" +
                        "<div class='section'>" +
                        "<h2>Время выполнения операций</h2>" +
                        "<div class='time-info'>" +
                        "<p><strong>Подсчет пользователей:</strong> %d мс</p>" +
                        "<p><strong>Получение списка объектов:</strong> %d мс</p>" +
                        "</div>" +
                        "<div class='total-time'>" +
                        "<p><strong>Общее время формирования отчета:</strong> %d мс</p>" +
                        "</div>" +
                        "</div>" +
                        "<div class='section'>" +
                        "<h2>Список объектов (мест)</h2>" +
                        "<table class='objects-table'>" +
                        "<thead>" +
                        "<tr>" +
                        "<th>ID</th>" +
                        "<th>Ряд</th>" +
                        "<th>Место</th>" +
                        "<th>Статус</th>" +
                        "</tr>" +
                        "</thead>" +
                        "<tbody>%s</tbody>" +
                        "</table>" +
                        "</div>",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
                userCountResult.result(),
                objectListResult.result().size(),
                userCountResult.executionTime(),
                objectListResult.executionTime(),
                totalElapsed,
                objectListHtml
        );
    }

    // Вспомогательный метод для экранирования HTML символов
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }

    private Integer calculateUserCount() {
        long count = userRepository.count();
        return (int) count;
    }

    private List<Seat> getObjectList() {
        return (List<Seat>) seatRepository.findAll();
    }
}