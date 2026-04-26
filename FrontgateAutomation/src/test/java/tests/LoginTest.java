package tests;

import pages.HomePage;
import pages.LoginPage;
import utils.TestData;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.time.Duration;

public class LoginTest {

    private WebDriver driver;
    private HomePage  homePage;
    private LoginPage loginPage;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver",
            "C:\\Users\\2022\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(TestData.BASE_URL);

        homePage  = new HomePage(driver);
        loginPage = new LoginPage(driver);
    }

    @Test(priority = 1)
    public void verifyHomePageElements() {
        Assert.assertTrue(homePage.isLogoDisplayed(),     "Logo should be visible");
        Assert.assertTrue(homePage.isMyAccountDisplayed(), "My Account should be visible");
        Assert.assertTrue(homePage.isMyBagDisplayed(),    "My Bag should be visible");
    }


    @Test(priority = 2)
    public void verifyLoginSuccess() {
        homePage.clickSignIn();

        new WebDriverWait(driver, Duration.ofSeconds(15)).until(
            driver -> !driver.getCurrentUrl().contains("frontgate.com/?")
        );

        System.out.println("Current URL: " + driver.getCurrentUrl());

        loginPage.dismissCookieBannerIfPresent();

        new WebDriverWait(driver, Duration.ofSeconds(15)).until(
            ExpectedConditions.elementToBeClickable(By.id("email"))
        );

        loginPage.login(TestData.EMAIL, TestData.PASSWORD);

        System.out.println("Login submitted, waiting for redirect...");


        new WebDriverWait(driver, Duration.ofSeconds(30)).until(
            driver -> !driver.getCurrentUrl().contains("UserLogonView")
        );

        System.out.println("After login URL: " + driver.getCurrentUrl());

        new WebDriverWait(driver, Duration.ofSeconds(30)).until(
            ExpectedConditions.visibilityOfElementLocated(By.id("my-account-button"))
        );

        System.out.println("Homepage loaded after login!");

        String welcomeMessage = homePage.getWelcomeMessage();
        System.out.println("Welcome message: " + welcomeMessage);

        Assert.assertTrue(
            welcomeMessage.contains(TestData.EXPECTED_FIRST_NAME),
            "Welcome message should contain: " + TestData.EXPECTED_FIRST_NAME
        );
    }
    @AfterClass
    public void tearDown() {
        System.out.println("Test finished. Final URL: " + driver.getCurrentUrl());
        try { Thread.sleep(5000); } catch (Exception ignored) {}
        if (driver != null) driver.quit();
    }
}