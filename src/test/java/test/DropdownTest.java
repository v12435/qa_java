package test;

import pageobject.MainPage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.WebDriver;
import util.DriverFactory;

public class DropdownTest {
    WebDriver driver;
    MainPage mainPage;

    @BeforeEach
    void setUp() {
        driver = DriverFactory.createDriver();
        driver.get("https://qa-scooter.praktikum-services.ru/");
        mainPage = new MainPage(driver);
        mainPage.acceptCookies();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @ParameterizedTest(name = "FAQ: {1}")
    @CsvSource({
            "0, Сколько это стоит? И как оплатить?",
            "1, Хочу сразу несколько самокатов! Так можно?",
            "2, Как рассчитывается время аренды?",
            "3, Можно ли заказать самокат прямо на сегодня?",
            "4, Можно ли продлить заказ или вернуть самокат раньше?",
            "5, Вы привозите зарядку вместе с самокатом?",
            "6, Можно ли отменить заказ?",
            "7, Я живу за МКАДом, привезёте?"
    })
    void testFaqAnswerNotEmpty(int index, String questionText) {
        mainPage.scrollToFaqQuestion(index);
        mainPage.clickFaqQuestion(index);
        String answer = mainPage.getFaqAnswer(index);

        System.out.println("Вопрос: " + questionText);
        System.out.println("Ответ: " + answer); // вывод в консоль ответов для отладки
        
        Assertions.assertFalse(answer.isEmpty(),
                "Ответ не должен быть пустым для вопроса: " + questionText); // содержимое ответов меняется, поэтому ассерт только на то, что поле не пустое
    }
}
