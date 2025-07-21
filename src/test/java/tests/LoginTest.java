package tests;

import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.testng.Assert;

public class LoginTest {

    WebDriver driver;
    LoginPage loginPage;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/");
        driver.manage().window().maximize();
        loginPage = new LoginPage(driver);
    }

    @Test
    public void validLogin() {
        loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"));
    }

    @Test
    public void invalidPassword() {
        loginPage.login("standard_user", "wrong_pass");
        assertErrorMessage("Epic sadface: Username and password do not match any user in this service");
    }

    @Test
    public void invalidUsername() {
        loginPage.login("wrong_user", "secret_sauce");
        assertErrorMessage("Epic sadface: Username and password do not match any user in this service");
    }

    @Test
    public void bothInvalid() {
        loginPage.login("invalid", "invalid");
        assertErrorMessage("Epic sadface: Username and password do not match any user in this service");
    }

    @Test
    public void emptyUsername() {
        loginPage.login("", "secret_sauce");
        assertErrorMessage("Epic sadface: Username is required");
    }

    @Test
    public void emptyPassword() {
        loginPage.login("standard_user", "");
        assertErrorMessage("Epic sadface: Password is required");
    }

    @Test
    public void emptyBothFields() {
        loginPage.login("", "");
        assertErrorMessage("Epic sadface: Username is required");
    }

    @Test
    public void lockedOutUser() {
        loginPage.login("locked_out_user", "secret_sauce");
        assertErrorMessage("Epic sadface: Sorry, this user has been locked out.");
    }

    @Test
    public void problemUserLogin() {
        loginPage.login("problem_user", "secret_sauce");
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"));
    }

    @Test
    public void usernameCaseSensitivity() {
        loginPage.login("Standard_User", "secret_sauce");
        assertErrorMessage("Epic sadface: Username and password do not match any user in this service");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    // Helper method to assert error messages
    private void assertErrorMessage(String expected) {
        String actual = driver.findElement(By.cssSelector("h3[data-test='error']")).getText();
        Assert.assertEquals(actual.trim(), expected.trim());
    }
}