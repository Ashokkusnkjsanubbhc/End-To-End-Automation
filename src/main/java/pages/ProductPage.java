
	package pages;

	import org.openqa.selenium.*;
	import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

	public class ProductPage {
	    WebDriver driver;

	    @FindBy(className = "title")
	    WebElement title;

	    @FindBy(css = ".inventory_item")
	    List<WebElement> allProducts;

	    @FindBy(css = ".btn_inventory")
	    List<WebElement> allAddToCartButtons;

	    @FindBy(className = "shopping_cart_badge")
	    WebElement cartBadge;

	    @FindBy(className = "product_sort_container")
	    WebElement sortDropdown;

	    @FindBy(css = ".inventory_item_name")
	    List<WebElement> productNames;

	    public ProductPage(WebDriver driver) {
	        this.driver = driver;
	        PageFactory.initElements(driver, this);
	    }

	    public String getPageTitle() {
	        return title.getText();
	    }

	    public int getProductCount() {
	        return allProducts.size();
	    }

	    public void addProductByIndex(int index) {
	        allAddToCartButtons.get(index).click();
	    }

	    public void removeProductByIndex(int index) {
	        allAddToCartButtons.get(index).click(); // Button text toggles to "Remove"
	    }

	    public int getCartItemCount() {
	        try {
	            return Integer.parseInt(cartBadge.getText());
	        } catch (NoSuchElementException e) {
	            return 0;
	        }
	    }

	    public void selectSortOption(String value) {
	        new Select(sortDropdown).selectByValue(value);
	    }

	    public void clickOnProductByIndex(int index) {
	        productNames.get(index).click();
	    }
	}

