package tests;

import pages.HomePage;
import pages.LoginPage;
import utils.ExcelReader;
import utils.TestData;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.time.Duration;

public class LoginDataDrivenTest {

    private WebDriver driver;
    private HomePage homePage;
    private LoginPage loginPage;

    private static final String EXCEL_PATH =
        "src/test/resources/testdata.xlsx";
    private static final String SHEET_NAME = "LoginData";

    private final By errorMsg = By.cssSelector(
        ".error-message, .alert, [class*='error'], [class*='alert']"
    );

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver",
            "C:\\Users\\2022\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        driver = new ChromeDriver();
        driver.manage().window().maximize();

        homePage  = new HomePage(driver);
        loginPage = new LoginPage(driver);
    }

    @Test
    public void runLoginTestsFromExcel() throws IOException, InterruptedException {

        ExcelReader excel = new ExcelReader(EXCEL_PATH, SHEET_NAME);
        int rowCount = excel.getRowCount();

        for (int i = 1; i <= rowCount; i++) {
            String email          = excel.getCellData(i, 0);
            String password       = excel.getCellData(i, 1);
            String expectedResult = excel.getCellData(i, 2);

            System.out.println("=== Test Case " + i + " ===");
            System.out.println("Email: " + email);
            System.out.println("Password: " + password);
            System.out.println("Expected: " + expectedResult);

            driver.get(TestData.BASE_URL);
            homePage.clickSignIn();

            new WebDriverWait(driver, Duration.ofSeconds(15)).until(
                d -> !d.getCurrentUrl().contains("frontgate.com/?")
            );

            new WebDriverWait(driver, Duration.ofSeconds(15)).until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("input[type='email']"))
            );

            loginPage.login(email, password);

            Thread.sleep(3000);

            switch (expectedResult) {
                case "invalid_email":
                    String currentUrl1 = driver.getCurrentUrl();
                    Assert.assertTrue(
                        currentUrl1.contains("UserLogonView") ||
                        isErrorDisplayed(),
                        "Case " + i + ": Should show invalid email error"
                    );
                    System.out.println("PASS: Invalid email case handled");
                    break;

                case "wrong_password":
                    Assert.assertTrue(
                        driver.getCurrentUrl().contains("UserLogonView") ||
                        isErrorDisplayed(),
                        "Case " + i + ": Should show wrong password error"
                    );
                    System.out.println("PASS: Wrong password case handled");
                    break;

                case "success":
                    // انتظر redirect
                    new WebDriverWait(driver, Duration.ofSeconds(30)).until(
                        d -> !d.getCurrentUrl().contains("UserLogonView")
                    );
                    String welcomeMsg = homePage.getWelcomeMessage();
                    System.out.println("Welcome: " + welcomeMsg);
                    Assert.assertTrue(
                        welcomeMsg.contains(TestData.EXPECTED_FIRST_NAME),
                        "Case " + i + ": Welcome message should contain name"
                    );
                    System.out.println("PASS: Login success case handled");
                    break;
            }

           
        } 
        excel.close();
    }

    private boolean isErrorDisplayed() {
        try {
            return driver.findElement(errorMsg).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}