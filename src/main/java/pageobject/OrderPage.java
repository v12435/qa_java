package pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class OrderPage {
    private final WebDriver driver;

    /*
    // Поле «Имя» — nameField — fillClientInfo()
    // Поле «Фамилия» — surnameField — fillClientInfo()
    // Поле «Адрес» — addressField — fillClientInfo()
    // Поле «Метро» — metroField, metroOption — fillClientInfo()
    // Поле «Телефон» — phoneField — fillClientInfo()
    // Кнопка «Далее» — nextButton — goToNextStep()
    // Поле «Дата» — dateField — fillOrderDetails()
    // Выпадающий срок аренды — rentalPeriod, rentalOption — fillOrderDetails()
    // Чекбокс «Чёрный цвет» — blackColor — fillOrderDetails()
    // Чекбокс «Серый цвет» — greyColor — fillOrderDetails()
    // Поле «Комментарий» — commentField — fillOrderDetails()
    // Кнопка «Заказать» — orderButton — submitOrder()
    // Кнопка подтверждения «Да» — confirmYesButton — submitOrder()
    // Заголовок модалки — confirmationModalHeader — getConfirmationText()
*/

    public OrderPage(WebDriver driver) {
        this.driver = driver;
    }

    // --- ШАГ 1 ---

    private final By nameField = By.xpath("//input[@placeholder='* Имя']");
    private final By surnameField = By.xpath("//input[@placeholder='* Фамилия']");
    private final By addressField = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");

    private final By metroField = By.className("select-search__input");
    private final By metroOption = By.xpath("//button[@value='1']"); // пример: первая станция

    private final By phoneField = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");

    private final By nextButton = By.xpath("//button[contains(text(), 'Далее')]");

    // --- ШАГ 2 ---

    private final By dateField = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    private final By rentalPeriod = By.className("Dropdown-placeholder");
    private final By rentalOption = By.xpath("//div[text()='сутки']");

    private final By blackColor = By.id("black");
    private final By greyColor = By.id("grey");

    private final By commentField = By.xpath("//input[@placeholder='Комментарий для курьера']");

    private final By orderButton = By.xpath("//div[@class='Order_Buttons__1xGrp']/button[contains(text(),'Заказать')]");
    private final By confirmYesButton = By.xpath("//button[contains(text(),'Да')]");

    // Подтверждение заказа
    private final By confirmationModalHeader = By.xpath("//div[@class='Order_ModalHeader__3FDaJ']");

    public void fillClientInfo(String name, String surname, String address, String phone) {
        driver.findElement(nameField).sendKeys(name);
        driver.findElement(surnameField).sendKeys(surname);
        driver.findElement(addressField).sendKeys(address);
        driver.findElement(metroField).click();
        driver.findElement(metroOption).click();
        driver.findElement(phoneField).sendKeys(phone);
    }

    public void goToNextStep() {
        driver.findElement(nextButton).click();
    }

    public void fillOrderDetails(String date, boolean black, boolean grey, String comment) {
        // Вводим дату и нажимаем Enter, чтобы выбрать её и закрыть календарь
        driver.findElement(dateField).sendKeys(date + Keys.ENTER);

        driver.findElement(rentalPeriod).click();
        driver.findElement(rentalOption).click();

        if (black) driver.findElement(blackColor).click();
        if (grey) driver.findElement(greyColor).click();

        driver.findElement(commentField).sendKeys(comment);
    }

    public void submitOrder() {
        driver.findElement(orderButton).click();
        driver.findElement(confirmYesButton).click();
    }

    public String getConfirmationText() {
        return driver.findElement(confirmationModalHeader).getText();
    }
}
