
	package tests;

	import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
	import org.testng.annotations.*;
	import pages.*;

	public class CheckoutTest {
	    WebDriver driver;
	    LoginPage loginPage;
	    ProductPage productPage;
	    CartPage cartPage;
	    CheckoutPage checkoutPage;
	    CheckoutOverviewPage overviewPage;
	    CheckoutCompletepage completePage;

	    @BeforeMethod
	    public void setup() {
	        driver = new ChromeDriver();
	        driver.get("https://www.saucedemo.com/");
	        driver.manage().window().maximize();

	        loginPage = new LoginPage(driver);
	        loginPage.login("standard_user", "secret_sauce");

	        productPage = new ProductPage(driver);
	        cartPage = new CartPage(driver);
	        checkoutPage = new CheckoutPage(driver);
	        overviewPage = new CheckoutOverviewPage(driver);
	        completePage = new CheckoutCompletepage();
	    }

	    private void goToCheckout() {
	        productPage.addProductByIndex(0);
	        driver.findElement(By.className("shopping_cart_link")).click();
	        cartPage.clickCheckout();
	    }

	    @Test
	    public void completeCheckoutSuccessfully() {
	        goToCheckout();
	        checkoutPage.enterCheckoutInfo("John", "Doe", "12345");

	        overviewPage.clickFinish();
	        Assert.assertEquals(completePage.getSuccessMessage(), "Thank you for your order!");
	    }

	    @Test
	    public void emptyFirstNameShowsError() {
	        goToCheckout();
	        checkoutPage.enterCheckoutInfo("", "Doe", "12345");
	        Assert.assertEquals(checkoutPage.getErrorMessage(), "Error: First Name is required");
	    }

	    @Test
	    public void emptyLastNameShowsError() {
	        goToCheckout();
	        checkoutPage.enterCheckoutInfo("John", "", "12345");
	        Assert.assertEquals(checkoutPage.getErrorMessage(), "Error: Last Name is required");
	    }

	    @Test
	    public void emptyPostalCodeShowsError() {
	        goToCheckout();
	        checkoutPage.enterCheckoutInfo("John", "Doe", "");
	        Assert.assertEquals(checkoutPage.getErrorMessage(), "Error: Postal Code is required");
	    }

	    @Test
	    public void verifyTotalPriceIncludesTax() {
	        goToCheckout();
	        checkoutPage.enterCheckoutInfo("John", "Doe", "12345");

	        String total = overviewPage.getTotalAmount();
	        Assert.assertTrue(total.contains("Total"));
	    }

	    @Test
	    public void finishButtonNavigatesToCompletePage() {
	        goToCheckout();
	        checkoutPage.enterCheckoutInfo("John", "Doe", "12345");

	        overviewPage.clickFinish();
	        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-complete"));
	    }

	    @Test
	    public void backToProductsNavigatesToInventory() {
	        goToCheckout();
	        checkoutPage.enterCheckoutInfo("John", "Doe", "12345");

	        overviewPage.clickFinish();
	        completePage.clickBackToProducts();
	        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"));
	    }

	    @Test
	    public void checkoutWithMultipleProducts() {
	        productPage.addProductByIndex(0);
	        productPage.addProductByIndex(1);
	        driver.findElement(By.className("shopping_cart_link")).click();

	        cartPage.clickCheckout();
	        checkoutPage.enterCheckoutInfo("Alice", "Smith", "56789");

	        overviewPage.clickFinish();
	        Assert.assertEquals(completePage.getSuccessMessage(), "Thank you for your order!");
	    }

	    @Test
	    public void checkoutWithoutAnyProduct() {
	        driver.findElement(By.className("shopping_cart_link")).click();
	        cartPage.clickCheckout();
	        checkoutPage.enterCheckoutInfo("No", "Item", "00000");

	        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-two")); // continues even if no item
	    }

	    @Test
	    public void invalidZipCodeFormatStillAccepted() {
	        goToCheckout();
	        checkoutPage.enterCheckoutInfo("Fake", "User", "abcde"); // no validation
	        overviewPage.clickFinish();

	        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-complete"));
	    }

	    @AfterMethod
	    public void tearDown() {
	        driver.quit();
	    }
	
}
