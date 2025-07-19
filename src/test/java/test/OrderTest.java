package test;

import pageobject.MainPage;
import pageobject.OrderPage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.WebDriver;
import util.DriverFactory;

public class OrderTest {
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

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @ParameterizedTest
    @CsvSource({
            "top, Иван, Иванов, Москва, 89999999999, 21.07.2025, true, false, Позвонить за час",
            "bottom, Петр, Петров, Санкт-Петербург, 88888888888, 22.07.2025, false, true, Не звонить",
            "top, Ольга, Смирнова, Казань, 91111111111, 23.07.2025, true, true, Без звонка",
            "bottom, Мария, Кузнецова, Новосибирск, 92222222222, 24.07.2025, false, false, Оставить у консьержа",
            "top, Алексей, Морозов, Екатеринбург, 93333333333, 25.07.2025, true, false, Доставить вечером",
            "bottom, Светлана, Соколова, Нижний Новгород, 94444444444, 26.07.2025, false, true, Не звонить ночью"
    })
    void testOrderViaButton(String buttonPosition, String name, String surname, String address,
                            String phone, String date, boolean black, boolean grey, String comment) {

        if ("top".equals(buttonPosition)) {
            mainPage.clickTopOrderButton();
        } else if ("bottom".equals(buttonPosition)) {
            mainPage.scrollToBottom();
            mainPage.clickBottomOrderButton();
        } else {
            throw new IllegalArgumentException("Unknown button position: " + buttonPosition);
        }

        orderPage.fillClientInfo(name, surname, address, phone);
        orderPage.goToNextStep();
        orderPage.fillOrderDetails(date, black, grey, comment);
        orderPage.submitOrder();

        String confirmationText = orderPage.getConfirmationText();
        System.out.println("Текст подтверждения: [" + confirmationText + "]");

        Assertions.assertTrue(
                confirmationText.contains("Заказ оформлен"),
                "Заказ не был оформлен. Вместо этого получено: " + confirmationText
        ); // баг в chrome- оформления заказа не происходит
    }
}
