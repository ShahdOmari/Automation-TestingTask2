package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions; 
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {

	private final By emailField = By.cssSelector("input[type='email']");

	private final By passwordField = By.cssSelector("input[type='password']");
    private final By loginButton   = By.cssSelector("button.login-button");
    private final By cookieClose   = By.cssSelector(
        "button[aria-label='Close'], button.close, #close-pc-btn-handler, button.cookie-close"
    );

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    private JavascriptExecutor js() {
        return (JavascriptExecutor) driver;
    }

    private void scrollToElement(WebElement element) {
        js().executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    private void jsSetValue(WebElement element, String value) {
        js().executeScript(
            "var el = arguments[0];" +
            "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(" +
            "    window.HTMLInputElement.prototype, 'value').set;" +
            "nativeInputValueSetter.call(el, arguments[1]);" +
            "el.dispatchEvent(new Event('input', { bubbles: true }));" +
            "el.dispatchEvent(new Event('change', { bubbles: true }));",
            element, value
        );
    }

    public void dismissCookieBannerIfPresent() {
        try {
            WebElement cookie = new WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(cookieClose));
            scrollToElement(cookie);
            cookie.click();
            System.out.println("Cookie banner closed!");
            new WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                .until(ExpectedConditions.invisibilityOf(cookie));
        } catch (Exception e) {
            System.out.println("No cookie banner: " + e.getMessage());
        }
    }

    public void enterEmail(String email) {
        WebElement field = wait.until(
            ExpectedConditions.presenceOfElementLocated(emailField)
        );
        scrollToElement(field);
        
        
        jsSetValue(field, email);
        System.out.println("Email entered: " + email);
    }

    public void enterPassword(String password) {
        WebElement field = wait.until(
            ExpectedConditions.presenceOfElementLocated(passwordField)
        );
        scrollToElement(field);
        
        jsSetValue(field, password);
        System.out.println("Password entered!");
    }

    public void clickLogin() {
        WebElement button = wait.until(
            ExpectedConditions.presenceOfElementLocated(loginButton)
        );
        scrollToElement(button);
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        js().executeScript("arguments[0].click();", button);
        System.out.println("Login button clicked!");
    }

    public void login(String email, String password) {
        dismissCookieBannerIfPresent();
        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }
}