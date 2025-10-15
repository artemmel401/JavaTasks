package ru.artem.NauJava.config;

import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.artem.NauJava.pocessor.CommandProcessor;


@Configuration
public class ProcessorConfig {
    @Autowired
    private CommandProcessor commandProcessor;
    @Bean
    public CommandLineRunner commandScanner()
    {
        return args ->
        {
            try (Scanner scanner = new Scanner(System.in))
            {
                System.out.println("=== Система бронирования билетов кинотеатра ===");
                System.out.println("Доступные команды:");
                System.out.println("  book <id> <movie> <dateTime><hall> <row> <seat> <name> <email> <price>");
                System.out.println("  list - показать все билеты");
                System.out.println("  find <id> - найти билет по ID");
                System.out.println("  cancel <id> - отменить бронирование");
                System.out.println("  customer <email> - билеты по email");
                System.out.println("  movie <title> - билеты по фильму");
                System.out.println("  update <id> <movie> <dateTime><hall> <row> <seat> <name> <email> <price> - обновление билета");
                System.out.println("  revenue - общая выручка");
                System.out.println("  count - количество билетов");
                System.out.println("  exit - выход");
                System.out.println("Введите команду:");
                while (true) {
                    System.out.print("> ");
                    String input = scanner.nextLine().trim();

                    if ("exit".equalsIgnoreCase(input)) {
                        System.out.println("Выход из программы...");
                        break;
                    }

                    if (!input.isEmpty()) {
                        commandProcessor.processCommand(input);
                    }
                }
            }
        };
    }
}

