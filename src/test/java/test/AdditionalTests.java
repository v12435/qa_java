package test;

import pageobject.MainPage;
import pageobject.OrderPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.time.Duration;
import java.util.stream.Stream;

public class AdditionalTests {
    WebDriver driver;
    MainPage mainPage;
    OrderPage orderPage;
//Факультативные тесты сделаны только под Chrome- не уложился в дедлайны
    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.get("https://qa-scooter.praktikum-services.ru/");
        mainPage = new MainPage(driver);
        mainPage.acceptCookies();
        orderPage = new OrderPage(driver);
    }

    @Test
    void testScooterLogoRedirectsToMainPage() {
        driver.get("https://qa-scooter.praktikum-services.ru/order");

        By scooterLogo = By.className("Header_LogoScooter__3lsAR");
        driver.findElement(scooterLogo).click();

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

        By yandexLogo = By.className("Header_LogoYandex__3TSOI");
        driver.findElement(yandexLogo).click();

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

        // Нажимаем "Далее" на пустой форме
        orderPage.goToNextStep();

        String actualError;
        if ("METRO".equals(placeholder)) {
            actualError = getMetroFieldErrorText();
        } else {
            actualError = getFieldErrorText(placeholder);
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
                Arguments.of("* Адрес: куда привезти заказ", "Введите адрес"), // такой ошибки не существует, поле не проверяется, но я решил оставить тест, чтобы он фейлился
                Arguments.of("METRO", "Выберите станцию"),
                Arguments.of("* Телефон: на него позвонит курьер", "Введите корректный номер")
        );
    }

    @Test
    void testOrderStatusNotFound() {
        By statusButton = By.className("Header_Link__1TAG7");
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(statusButton))
                .click();

        By orderNumberField = By.xpath("//input[@placeholder='Введите номер заказа']");
        WebElement orderNumberInput = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(orderNumberField));
        orderNumberInput.sendKeys("999999999");

        By goButton = By.xpath("//button[contains(text(), 'Go!')]");
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(goButton))
                .click();

        By notFoundBlock = By.className("Track_NotFound__6oaoY");
        boolean isNotFoundVisible = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(notFoundBlock))
                .isDisplayed();

        Assertions.assertTrue(isNotFoundVisible, "Блок о ненайденном заказе не появился на странице");
    }


    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Методы для проверки ошибок под полями

    private String getFieldErrorText(String fieldPlaceholder) {
        By errorLocator = By.xpath(
                "//input[@placeholder='" + fieldPlaceholder + "']/following-sibling::div[contains(@class,'Input_ErrorMessage__3HvIb')]"
        );
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(errorLocator))
                    .getText().trim();
        } catch (TimeoutException e) {
            return "";
        }
    }

    private String getMetroFieldErrorText() {
        By errorLocator = By.className("Order_MetroError__1BtZb");
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(errorLocator))
                    .getText().trim();
        } catch (TimeoutException e) {
            return "";
        }
    }
}
