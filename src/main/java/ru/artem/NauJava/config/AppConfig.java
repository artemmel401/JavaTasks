package ru.artem.NauJava.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import ru.artem.NauJava.entity.Ticket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfig {

    // Закомментируй пока не создашь application.properties
    // @Value("${app.name}")
    private String appName = "Cinema Booking System"; // значение по умолчанию

    // @Value("${app.version}")
    private String appVersion = "1.0.0"; // значение по умолчанию

    @Bean
    public AppInfo appInfo() {
        return new AppInfo(appName, appVersion);
    }

    @PostConstruct
    public void printAppInfo() {
        System.out.println("Имя приложения: " + appName);
        System.out.println("Версия приложения: " + appVersion);
    }

    @Bean
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public List<Ticket> ticketContainer() {
        return new ArrayList<>();
    }

    public record AppInfo(String name, String version) {
    }
}
