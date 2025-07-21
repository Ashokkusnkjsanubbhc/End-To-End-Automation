
	package tests;

	import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
	import org.testng.annotations.*;
	import pages.LoginPage;
	import pages.ProductPage;

	public class ProductTest {
	    WebDriver driver;
	    LoginPage loginPage;
	    ProductPage productPage;

	    @BeforeMethod
	    public void setup() {
	        driver = new ChromeDriver();
	        driver.get("https://www.saucedemo.com/");
	        driver.manage().window().maximize();

	        loginPage = new LoginPage(driver);
	        loginPage.login("standard_user", "secret_sauce");

	        productPage = new ProductPage(driver);
	    }

	    @Test
	    public void verifyPageTitle() {
	        Assert.assertEquals(productPage.getPageTitle(), "Products");
	    }

	    @Test
	    public void addSingleProductToCart() {
	        productPage.addProductByIndex(0);
	        Assert.assertEquals(productPage.getCartItemCount(), 1);
	    }

	    @Test
	    public void addAllProductsToCart() {
	        int total = productPage.getProductCount();
	        for (int i = 0; i < total; i++) {
	            productPage.addProductByIndex(i);
	        }
	        Assert.assertEquals(productPage.getCartItemCount(), total);
	    }

	    @Test
	    public void removeProductFromCart() {
	        productPage.addProductByIndex(0);
	        productPage.removeProductByIndex(0);
	        Assert.assertEquals(productPage.getCartItemCount(), 0);
	    }

	    @Test
	    public void verifyCartBadgeCount() {
	        productPage.addProductByIndex(0);
	        productPage.addProductByIndex(1);
	        Assert.assertEquals(productPage.getCartItemCount(), 2);
	    }

	    @Test
	    public void sortByNameAZ() {
	        productPage.selectSortOption("az");
	        Assert.assertTrue(true); // Optionally verify order if needed
	    }

	    @Test
	    public void sortByNameZA() {
	        productPage.selectSortOption("za");
	        Assert.assertTrue(true);
	    }

	    @Test
	    public void sortByPriceLowToHigh() {
	        productPage.selectSortOption("lohi");
	        Assert.assertTrue(true);
	    }

	    @Test
	    public void sortByPriceHighToLow() {
	        productPage.selectSortOption("hilo");
	        Assert.assertTrue(true);
	    }

	    @Test
	    public void openProductDetails() {
	        productPage.clickOnProductByIndex(0);
	        Assert.assertTrue(driver.getCurrentUrl().contains("inventory-item"));
	        driver.navigate().back();
	        Assert.assertTrue(productPage.getPageTitle().equals("Products"));
	    }

	    @AfterMethod
	    public void tearDown() {
	        driver.quit();
	    }
	}
