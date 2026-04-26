package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver; 
import java.time.Duration;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage extends BasePage {

    // --- Locators ---
    private final By logo      = By.cssSelector("a[title='Frontgate LOGO']");
    private final By myAccount = By.id("my-account-button");
    private final By myBag     = By.cssSelector("button[data-analytics-name='show_mini_cart']");
    private final By signInLink = By.cssSelector("a[title='Sign In / Register']");
    private final By welcomeMsg = By.cssSelector("div.c-list-tile__content-welcome");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isLogoDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(logo)).isDisplayed();
    }

    public boolean isMyAccountDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(myAccount)).isDisplayed();
    }

    public boolean isMyBagDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(myBag)).isDisplayed();
    }

    public void clickSignIn() {
        WebElement accountBtn = wait.until(
            ExpectedConditions.visibilityOfElementLocated(myAccount)
        );
        new Actions(driver).moveToElement(accountBtn).perform();
        wait.until(ExpectedConditions.elementToBeClickable(signInLink)).click();
    }

    public String getWelcomeMessage() {
        WebElement accountBtn = new WebDriverWait(driver, Duration.ofSeconds(20))
            .until(ExpectedConditions.visibilityOfElementLocated(myAccount));
        
        new Actions(driver).moveToElement(accountBtn).perform();
        
       
        return new WebDriverWait(driver, Duration.ofSeconds(20))
            .until(ExpectedConditions.visibilityOfElementLocated(welcomeMsg))
            .getText();
    }
}