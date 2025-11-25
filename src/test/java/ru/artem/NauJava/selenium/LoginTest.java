package ru.artem.NauJava.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromiumdriver().setup();
    }

    @BeforeEach
    void setupTest() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testSuccessfulLoginAndLogout() {
        driver.get("http://localhost:8080/login");

        WebElement usernameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("username"))
        );
        usernameField.sendKeys("Artem");

        WebElement passwordField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("password"))
        );
        passwordField.sendKeys("123");

        WebElement loginButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("primary"))
        );
        loginButton.click();

        WebElement userTableTitle = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.tagName("h1"))
        );

        Assertions.assertEquals("http://localhost:8080/", driver.getCurrentUrl());
        Assertions.assertEquals("User List", userTableTitle.getText());

        WebElement logoutButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("logout"))
        );
        logoutButton.click();

        Assertions.assertEquals("http://localhost:8080/login?logout", driver.getCurrentUrl());
    }

}
