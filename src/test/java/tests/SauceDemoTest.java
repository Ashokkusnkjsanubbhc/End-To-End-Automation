package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;

public class SauceDemoTest {
    WebDriver driver;
    LoginPage loginPage;
    ProductPage productPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;
    CheckoutOverviewPage overviewPage;
    CheckoutCompletepage completePage;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");

        loginPage = new LoginPage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        overviewPage = new CheckoutOverviewPage(driver);
        completePage = new CheckoutCompletepage();
    }

    // ---------- Data Providers ----------
    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginData() {
        return new Object[][] {
            {"standard_user", "wrong_pass", "Epic sadface: Username and password do not match any user in this service"},
            {"wrong_user", "secret_sauce", "Epic sadface: Username and password do not match any user in this service"},
            {"invalid", "invalid", "Epic sadface: Username and password do not match any user in this service"},
            {"", "secret_sauce", "Epic sadface: Username is required"},
            {"standard_user", "", "Epic sadface: Password is required"},
            {"", "", "Epic sadface: Username is required"},
            {"locked_out_user", "secret_sauce", "Epic sadface: Sorry, this user has been locked out."},
            {"Standard_User", "secret_sauce", "Epic sadface: Username and password do not match any user in this service"}
        };
    }

    @DataProvider(name = "checkoutUserData")
    public Object[][] checkoutUserData() {
        return new Object[][] {
            {"", "", "", "Error: First Name is required"},
            {"John", "", "", "Error: Last Name is required"},
            {"John", "Doe", "", "Error: Postal Code is required"},
            {"John", "Doe", "12345", "PASS"} // Valid
        };
    }

    // ---------- Login Tests ----------
    @Test(priority = 1, dataProvider = "invalidLoginData")
    public void testInvalidLogins(String username, String password, String expectedError) throws InterruptedException {
        driver.navigate().to("https://www.saucedemo.com/");
        Thread.sleep(500);
        loginPage.login(username, password);
        Thread.sleep(500);
        assertErrorMessage(expectedError);
        Thread.sleep(500);
    }

    @Test(priority = 2)
    public void testValidLogin() throws InterruptedException {
        driver.navigate().to("https://www.saucedemo.com/");
        Thread.sleep(500);
        loginPage.login("standard_user", "secret_sauce");
        Thread.sleep(500);
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"));
        Thread.sleep(500);
    }

    // ---------- Product Page Tests ----------
    @Test(priority = 3)
    public void testProductPageAddItemAndCartCount() throws InterruptedException {
        loginPage.login("standard_user", "secret_sauce");
        Thread.sleep(500);
        Assert.assertEquals(productPage.getPageTitle(), "Products");
        Thread.sleep(500);
        productPage.addProductByIndex(0);
        Thread.sleep(500);
        Assert.assertEquals(productPage.getCartItemCount(), 1);
        Thread.sleep(500);
    }

    // ---------- Cart Page Tests ----------
    @Test(priority = 4)
    public void testCartPageNavigation() throws InterruptedException {
        driver.findElement(By.className("shopping_cart_link")).click();
        Thread.sleep(500);
        Assert.assertTrue(driver.getCurrentUrl().contains("cart"));
        Thread.sleep(500);
        cartPage.clickCheckout();
        Thread.sleep(500);
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-one"));
        Thread.sleep(500);
    }

    // ---------- Checkout Info Page Tests ----------
    @Test(priority = 5, dataProvider = "checkoutUserData")
    public void testCheckoutUserInformation(String fname, String lname, String zip, String expectedMsg) throws InterruptedException {
        driver.navigate().to("https://www.saucedemo.com/");
        Thread.sleep(500);
        loginPage.login("standard_user", "secret_sauce");
        Thread.sleep(500);
        productPage.addProductByIndex(0);
        Thread.sleep(500);
        driver.findElement(By.className("shopping_cart_link")).click();
        Thread.sleep(500);
        cartPage.clickCheckout();
        Thread.sleep(500);

        checkoutPage.enterCheckoutInfo(fname, lname, zip);
        Thread.sleep(500);
        if (!expectedMsg.equals("PASS")) {
            String actualError = driver.findElement(By.cssSelector("h3[data-test='error']")).getText();
            Assert.assertEquals(actualError.trim(), expectedMsg.trim());
        } else {
            Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-two"));
        }
        Thread.sleep(500);
    }

    // ---------- Checkout Overview & Completion ----------
    @Test(priority = 6)
    public void testFinishCheckoutFlow() throws InterruptedException {
        driver.navigate().to("https://www.saucedemo.com/");
        Thread.sleep(500);
        loginPage.login("standard_user", "secret_sauce");
        Thread.sleep(500);
        productPage.addProductByIndex(0);
        Thread.sleep(500);
        driver.findElement(By.className("shopping_cart_link")).click();
        Thread.sleep(500);
        cartPage.clickCheckout();
        Thread.sleep(500);
        checkoutPage.enterCheckoutInfo("John", "Doe", "12345");
        Thread.sleep(500);
        overviewPage.clickFinish();
        Thread.sleep(500);
        Assert.assertEquals(completePage.getSuccessMessage(), "Thank you for your order!");
        Thread.sleep(500);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void assertErrorMessage(String expected) {
        String actual = driver.findElement(By.cssSelector("h3[data-test='error']")).getText();
        Assert.assertEquals(actual.trim(), expected.trim());
    }
}