
	package tests;

	import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
	import org.testng.Assert;
	import org.testng.annotations.*;
	import pages.LoginPage;
	import pages.ProductPage;

	public class ProductUITest {
	    WebDriver driver;
	    LoginPage loginPage;
	    ProductPage productPage;

	    @BeforeMethod
	    public void setup() {
	        driver = new ChromeDriver();
	        driver.manage().window().maximize();
	        driver.get("https://www.saucedemo.com/");
	        loginPage = new LoginPage(driver);
	        loginPage.login("standard_user", "secret_sauce");
	        productPage = new ProductPage(driver);
	    }

	    @Test
	    public void checkPageTitleText() {
	        Assert.assertEquals(productPage.getPageTitle(), "Products");
	    }

	    @Test
	    public void verifyLogoIsVisible() {
	        boolean logoVisible = driver.findElement(By.className("app_logo")).isDisplayed();
	        Assert.assertTrue(logoVisible);
	    }

	    @Test
	    public void scrollToBottomAndVerifyFooter() {
	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	        WebElement footer = driver.findElement(By.className("footer_copy"));
	        Assert.assertTrue(footer.isDisplayed());
	    }

	    @Test
	    public void checkFontSizeOfProductTitle() {
	        WebElement title = driver.findElement(By.className("inventory_item_name"));
	        String fontSize = title.getCssValue("font-size");
	        Assert.assertEquals(fontSize, "20px"); // Check value using browser's dev tools
	    }

	    @Test
	    public void checkPriceColor() {
	        WebElement price = driver.findElement(By.className("inventory_item_price"));
	        String color = price.getCssValue("color");
	        Assert.assertTrue(color.contains("rgb"));
	    }

	    @Test
	    public void validateImagesLoaded() {
	        WebElement image = driver.findElement(By.cssSelector(".inventory_item_img img"));
	        boolean loaded = (Boolean) ((JavascriptExecutor) driver).executeScript(
	            "return arguments[0].complete && arguments[0].naturalWidth > 0;", image
	        );
	        Assert.assertTrue(loaded);
	    }

	    @Test
	    public void verifyInputSanitizationInCheckout() {
	        driver.findElement(By.className("shopping_cart_link")).click();
	        driver.findElement(By.id("checkout")).click();

	        driver.findElement(By.id("first-name")).sendKeys("<script>alert(1)</script>");
	        driver.findElement(By.id("last-name")).sendKeys("X");
	        driver.findElement(By.id("postal-code")).sendKeys("000");
	        driver.findElement(By.id("continue")).click();

	        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-two"));
	    }

	    @AfterMethod
	    public void tearDown() {
	        driver.quit();
	    }
	
}
