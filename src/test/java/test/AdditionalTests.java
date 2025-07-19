package test;

import pageobject.MainPage;
import pageobject.OrderPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import util.DriverFactory;

import java.time.Duration;
import java.util.stream.Stream;

public class AdditionalTests {
    WebDriver driver;
    MainPage mainPage;
    OrderPage orderPage;

    @BeforeEach
    void setUp() {
        driver = DriverFactory.createDriver();
        driver.get("https://qa-scooter.praktikum-services.ru/");
        mainPage = new MainPage(driver);
        orderPage = new OrderPage(driver);
        mainPage.acceptCookies();
    }

    @Test
    void testScooterLogoRedirectsToMainPage() {
        driver.get("https://qa-scooter.praktikum-services.ru/order");
        mainPage.clickScooterLogo();
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertEquals(
                "https://qa-scooter.praktikum-services.ru/",
                currentUrl,
                "Клик по логотипу Самоката не перенаправил на главную страницу"
        );
    }

    @Test
    void testYandexLogoOpensNewWindow() {
        String originalWindow = driver.getWindowHandle();
        mainPage.clickYandexLogo();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(d -> driver.getWindowHandles().size() > 1);

        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        String currentUrl = driver.getCurrentUrl();
        Assertions.assertNotNull(currentUrl, "currentUrl вернул null");

        Assertions.assertTrue(
                currentUrl.contains("yandex.ru"),
                "После клика по логотипу Яндекса не открылась страница Яндекса. Оказались на странице: " + currentUrl
        );

        /*
    Здесь всегда открывается сайт Дзена,
    а в описании задачи должен открываться сайт Яндекса.
    Тест всегда выдаёт ошибку.
    Можно исправить ожидаемый результат на dzen.ru,
    тогда тест будет проходить.
*/

        driver.close();
        driver.switchTo().window(originalWindow);
    }

    @ParameterizedTest(name = "Проверка ошибки для поля {0}")
    @MethodSource("fieldsAndExpectedErrors")
    void testOrderFormFieldErrors(String placeholder, String expectedError) {
        mainPage.clickTopOrderButton();
        orderPage.goToNextStep();

        String actualError;
        if ("METRO".equals(placeholder)) {
            actualError = orderPage.getMetroFieldErrorText();
        } else {
            actualError = orderPage.getFieldErrorText(placeholder);
        }

        Assertions.assertEquals(
                expectedError,
                actualError,
                "Ошибка под полем [" + placeholder + "] не совпадает с ожидаемой"
        );
    }

    static Stream<Arguments> fieldsAndExpectedErrors() {
        return Stream.of(
                Arguments.of("* Имя", "Введите корректное имя"),
                Arguments.of("* Фамилия", "Введите корректную фамилию"),
                Arguments.of("* Адрес: куда привезти заказ", "Введите адрес"),
                Arguments.of("METRO", "Выберите станцию"),
                Arguments.of("* Телефон: на него позвонит курьер", "Введите корректный номер")
        );
    }

    @Test
    void testOrderStatusNotFound() {
        mainPage.clickOrderStatusButton();
        mainPage.enterOrderNumber("999999999");
        mainPage.clickGoButton();

        boolean isNotFoundVisible = mainPage.isNotFoundMessageDisplayed();
        Assertions.assertTrue(isNotFoundVisible, "Блок о ненайденном заказе не появился на странице");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
