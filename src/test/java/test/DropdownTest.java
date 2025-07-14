package test;

import pageobject.MainPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DropdownTest {
    WebDriver driver;
    MainPage mainPage;

    @Test
    void testAllFaqItemsNotEmptyInChrome() {
        runTestInBrowser("chrome");
    }

    @Test
    void testAllFaqItemsNotEmptyInFirefox() {
        runTestInBrowser("firefox");
    }

    private void runTestInBrowser(String browser) {
        try {
            driver = createDriver(browser);

            driver.get("https://qa-scooter.praktikum-services.ru/");
            mainPage = new MainPage(driver);
            mainPage.acceptCookies();

            int count = mainPage.getFaqQuestionsCount();
            System.out.println("[" + browser + "] Всего вопросов в FAQ: " + count);

            for (int i = 0; i < count; i++) {
                System.out.println("[" + browser + "] Проверяем вопрос #" + i);
                mainPage.scrollToFaqQuestion(i);
                mainPage.clickFaqQuestion(i);
                String answer = mainPage.getFaqAnswer(i);
                System.out.println("[" + browser + "] Вопрос #" + i + " → ответ: [" + answer + "]");
                Assertions.assertFalse(answer.isEmpty(),
                        "[" + browser + "] Ответ не должен быть пустым для вопроса #" + i);
            }
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private WebDriver createDriver(String browser) {
        switch (browser) {
            case "chrome":
                return new ChromeDriver();
            case "firefox":
                return new FirefoxDriver();
            default:
                throw new RuntimeException("Unknown browser: " + browser);
        }
    }
}
