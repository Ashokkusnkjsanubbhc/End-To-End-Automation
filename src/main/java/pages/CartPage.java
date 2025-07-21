
	package pages;

	import org.openqa.selenium.*;
	import org.openqa.selenium.support.*;
	import java.util.List;

	public class CartPage {
	    WebDriver driver;

	    @FindBy(className = "cart_item")
	    List<WebElement> cartItems;

	    @FindBy(className = "inventory_item_name")
	    List<WebElement> productNames;

	    @FindBy(className = "inventory_item_price")
	    List<WebElement> productPrices;

	    @FindBy(id = "continue-shopping")
	    WebElement continueShoppingBtn;

	    @FindBy(id = "checkout")
	    WebElement checkoutBtn;

	    public CartPage(WebDriver driver) {
	        this.driver = driver;
	        PageFactory.initElements(driver, this);
	    }

	    public int getCartItemCount() {
	        return cartItems.size();
	    }

	    public String getProductName(int index) {
	        return productNames.get(index).getText();
	    }

	    public String getProductPrice(int index) {
	        return productPrices.get(index).getText();
	    }

	    public void clickContinueShopping() {
	        continueShoppingBtn.click();
	    }

	    public void clickCheckout() {
	        checkoutBtn.click();
	    }

	    public void removeItemByName(String productName) {
	        WebElement removeBtn = driver.findElement(
	            By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='cart_item']//button")
	        );
	        removeBtn.click();
	    }
	
}
