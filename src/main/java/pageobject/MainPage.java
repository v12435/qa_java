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
        WebElement element = driver.findElements(faqList).get(index);
        element.click();
    }

    public String getFaqAnswer(int index) {
        WebElement element = driver.findElements(faqAnswers).get(index);

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(driver -> !element.getText().trim().isEmpty());

        return element.getText().trim();
    }

    public int getFaqQuestionsCount() {
        return driver.findElements(faqList).size();
    }

    public void clickTopOrderButton() {
        WebElement element = driver.findElement(topOrderButton);
        element.click();
    }

    public void clickBottomOrderButton() {
        WebElement element = driver.findElement(bottomOrderButton);
        element.click(); //не используется в тестах
    }
}
