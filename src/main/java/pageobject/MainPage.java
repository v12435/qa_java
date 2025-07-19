package pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MainPage {
    private final WebDriver driver;


/*
    // Баннер cookies — cookieButton — acceptCookies()
    // Кнопка «Заказать» (верх) — topOrderButton — clickTopOrderButton()
    // Кнопка «Заказать» (низ) — bottomOrderButton — clickBottomOrderButton()
    // Блок FAQ — faqList — scrollToFaqQuestion(), clickFaqQuestion(), getFaqQuestionsCount()
    // Ответы FAQ — faqAnswers — getFaqAnswer()
*/

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    private final By cookieButton = By.id("rcc-confirm-button");
    private final By faqList = By.cssSelector("div.Home_FAQ__3uVm4 div.accordion__button");
    private final By faqAnswers = By.cssSelector("div.Home_FAQ__3uVm4 div.accordion__panel");
    private final By topOrderButton = By.cssSelector("div.Header_Nav__AGCXC > button.Button_Button__ra12g");
    private final By bottomOrderButton = By.cssSelector("div.Home_FinishButton__1_cWm > button.Button_Button__ra12g");
    private final By scooterLogo = By.className("Header_LogoScooter__3lsAR");
    private final By yandexLogo = By.className("Header_LogoYandex__3TSOI");
    private final By orderStatusButton = By.className("Header_Link__1TAG7");
    private final By orderNumberField = By.xpath("//input[@placeholder='Введите номер заказа']");
    private final By goButton = By.xpath("//button[contains(text(), 'Go!')]");
    private final By notFoundBlock = By.className("Track_NotFound__6oaoY");


    public void acceptCookies() {
        List<WebElement> cookies = driver.findElements(cookieButton);
        if (!cookies.isEmpty()) {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(cookieButton));
            cookies.get(0).click();
            System.out.println("Баннер куки закрыт.");
        } else {
            System.out.println("Баннер куки не появился.");
        }
    }

    public void scrollToFaqQuestion(int index) {
        WebElement element = driver.findElements(faqList).get(index);
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(false);", element);
        try {
            Thread.sleep(300); // пауза помогает избежать перекрытий
        } catch (InterruptedException e) {
            System.out.println("Ошибка при скролле FAQ: " + e.getMessage());
        }
    }

    public void clickFaqQuestion(int index) {
        driver.findElements(faqList).get(index).click();
    }


    public String getFaqAnswer(int index) {
        WebElement element = driver.findElements(faqAnswers).get(index);

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(driver -> !element.getText().trim().isEmpty());

        return element.getText().trim();
    }

    public void clickTopOrderButton() {
        driver.findElement(topOrderButton).click();
    }


    public void clickBottomOrderButton() {
        WebElement element = driver.findElement(bottomOrderButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        try {
            Thread.sleep(300); // чтобы заголовок успел уехать вверх
        } catch (InterruptedException e) {
            // log it
        }
        element.click();
    }

    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        try {
            Thread.sleep(300); // небольшая задержка для прогрузки
        } catch (InterruptedException e) {
            System.out.println("Ошибка при скролле вниз: " + e.getMessage());
        }
    }

    public void clickScooterLogo() {
        driver.findElement(scooterLogo).click();
    }

    public void clickYandexLogo() {
        driver.findElement(yandexLogo).click();
    }

    public void clickOrderStatusButton() {
        driver.findElement(orderStatusButton).click();
    }

    public void enterOrderNumber(String number) {
        WebElement input = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(orderNumberField));
        input.sendKeys(number);
    }

    public void clickGoButton() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(goButton))
                .click();
    }

    public boolean isNotFoundMessageDisplayed() {
        return new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(notFoundBlock))
                .isDisplayed();
    }

}
