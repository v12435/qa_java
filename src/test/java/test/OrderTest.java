package test;

import pageobject.MainPage;
import pageobject.OrderPage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class OrderTest {
    WebDriver driver;
    MainPage mainPage;
    OrderPage orderPage;

    @BeforeEach
    void setUp() {
        // Инициализацию драйвера делаем в самом тесте, чтобы брать браузер из параметра
    }

    @ParameterizedTest
    @CsvSource({
            "chrome, Иван, Иванов, Москва, 89999999999, 21.07.2025, true, false, Позвонить за час",
            "firefox, Петр, Петров, Санкт-Петербург, 88888888888, 22.07.2025, false, true, Не звонить",
            "chrome, Ольга, Смирнова, Казань, 91111111111, 23.07.2025, true, true, Без звонка",
            "firefox, Мария, Кузнецова, Новосибирск, 92222222222, 24.07.2025, false, false, Оставить у консьержа",
            "chrome, Алексей, Морозов, Екатеринбург, 93333333333, 25.07.2025, true, false, Доставить вечером",
            "firefox, Светлана, Соколова, Нижний Новгород, 94444444444, 26.07.2025, false, true, Не звонить ночью"
    })
    void testOrderViaTopButton(String browser, String name, String surname, String address,
                               String phone, String date, boolean black, boolean grey, String comment) {

        try {
            if (browser.equals("chrome")) {
                driver = new ChromeDriver();
            } else if (browser.equals("firefox")) {
                driver = new FirefoxDriver();
            } else {
                throw new RuntimeException("Unknown browser: " + browser);
            }

            driver.get("https://qa-scooter.praktikum-services.ru/");
            mainPage = new MainPage(driver);
            orderPage = new OrderPage(driver);
            mainPage.acceptCookies();

            mainPage.clickTopOrderButton();
            orderPage.fillClientInfo(name, surname, address, phone);
            orderPage.goToNextStep();
            orderPage.fillOrderDetails(date, black, grey, comment);
            orderPage.submitOrder();

            String confirmationText = orderPage.getConfirmationText();
            System.out.println("Текст подтверждения: [" + confirmationText + "]");

            Assertions.assertTrue(
                    confirmationText.contains("Заказ оформлен"),
                    "Заказ не был оформлен. Вместо этого получено: " + confirmationText
            );
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
