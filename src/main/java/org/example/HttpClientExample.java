package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.time.Duration;

    /*Я пытался импортировать библиотеку jackson - вот так:<dependencies>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.13.5.redhat-00002</version>
        </dependency>
    </dependencies>, но у меня выводилась ошибка, что такого пакета нет, подскажите пожалуйста в комментариях,
    что я сделал не так:(*/

public class HttpClientExample {
    public static void sendHttpRequest(String urlString) throws IOException, InterruptedException {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlString))
                    .header("User-Agent", "Java-HTTP-Client/1.0")
                    .header("Accept", "application/json, text/html, application/xml")
                    .header("Accept-Language", "en-US,en;q=0.5")
                    .header("Accept-Encoding", "gzip, deflate")
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Код ответа HTTP: " + response.statusCode());
                System.out.println("\n=== Полный ответ сервера ===");
                System.out.println(response.body());

                System.out.println("\n=== Accept заголовки из ответа ===");
                String json = response.body();
                findAndPrintHeader(json, "Accept");
                findAndPrintHeader(json, "Accept-Language");
                findAndPrintHeader(json, "Accept-Encoding");
                findAndPrintHeader(json, "Accept-Charset");
            } else {
                throw new RuntimeException("HTTP ошибка: " + response.statusCode());
            }
        }
    }

    private static void findAndPrintHeader(String json, String headerName) {

        String pattern = "\"" + headerName + "\": \"";
        int start = json.indexOf(pattern);
        if (start != -1) {
            start += pattern.length();
            int end = json.indexOf("\"", start);
            if (end != -1) {
                String value = json.substring(start, end);
                System.out.println(headerName + ": " + value);
            }
        }
    }
}