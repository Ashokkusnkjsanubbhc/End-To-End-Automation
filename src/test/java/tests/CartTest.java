
	package tests;

	import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
	import org.testng.annotations.*;
	import pages.LoginPage;
	import pages.ProductPage;
	import pages.CartPage;

	public class CartTest {
	    WebDriver driver;
	    LoginPage loginPage;
	    ProductPage productPage;
	    CartPage cartPage;

	    @BeforeClass
	    public void setup() {
	        driver = new ChromeDriver();
	        driver.get("https://www.saucedemo.com/");
	        driver.manage().window().maximize();

	        loginPage = new LoginPage(driver);
	        loginPage.login("standard_user", "secret_sauce");

	        productPage = new ProductPage(driver);
	        cartPage = new CartPage(driver);
	    }

	    @Test
	    public void addProductAndVerifyInCart() {
	        productPage.addProductByIndex(0);
	        driver.findElement(By.className("shopping_cart_link")).click();

	        Assert.assertEquals(cartPage.getCartItemCount(), 1);
	    }

	    @Test
	    public void verifyProductNameInCart() {
	        productPage.addProductByIndex(0);
	        String expectedName = driver.findElements(By.className("inventory_item_name")).get(0).getText();

	        driver.findElement(By.className("shopping_cart_link")).click();
	        Assert.assertEquals(cartPage.getProductName(0), expectedName);
	    }

	    @Test
	    public void verifyProductPriceInCart() {
	        productPage.addProductByIndex(0);
	        String expectedPrice = driver.findElements(By.className("inventory_item_price")).get(0).getText();

	        driver.findElement(By.className("shopping_cart_link")).click();
	        Assert.assertEquals(cartPage.getProductPrice(0), expectedPrice);
	    }

	    @Test
	    public void removeItemFromCartPage() {
	        productPage.addProductByIndex(0);
	        driver.findElement(By.className("shopping_cart_link")).click();

	        String productName = cartPage.getProductName(0);
	        cartPage.removeItemByName(productName);

	        Assert.assertEquals(cartPage.getCartItemCount(), 0);
	    }

	    @Test
	    public void continueShoppingReturnsToProducts() {
	        productPage.addProductByIndex(0);
	        driver.findElement(By.className("shopping_cart_link")).click();

	        cartPage.clickContinueShopping();
	        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"));
	    }

	    @Test
	    public void checkoutButtonNavigatesToCheckoutStepOne() {
	        productPage.addProductByIndex(0);
	        driver.findElement(By.className("shopping_cart_link")).click();

	        cartPage.clickCheckout();
	        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-one"));
	    }

	    @Test
	    public void addMultipleItemsToCartAndVerify() {
	        productPage.addProductByIndex(0);
	        productPage.addProductByIndex(1);
	        driver.findElement(By.className("shopping_cart_link")).click();

	        Assert.assertEquals(cartPage.getCartItemCount(), 2);
	    }

	    @Test
	    public void freshLoginHasEmptyCart() {
	        driver.findElement(By.className("shopping_cart_link")).click();
	        Assert.assertEquals(cartPage.getCartItemCount(), 0);
	    }

	    @Test
	    public void cartPageWithoutAddingItems() {
	        driver.findElement(By.className("shopping_cart_link")).click();
	        Assert.assertEquals(cartPage.getCartItemCount(), 0);
	    }

	    @Test
	    public void badgeCountMatchesCartItems() {
	        productPage.addProductByIndex(0);
	        productPage.addProductByIndex(1);

	        String badgeText = driver.findElement(By.className("shopping_cart_badge")).getText();
	        Assert.assertEquals(Integer.parseInt(badgeText), 2);
	    }

	    @AfterMethod
	    public void tearDown() {
	        driver.quit();
	    }
	
}
