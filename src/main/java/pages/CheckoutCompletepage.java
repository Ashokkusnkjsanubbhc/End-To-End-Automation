

	package pages;

	import org.openqa.selenium.*;
	import org.openqa.selenium.support.*;

	public class CheckoutCompletepage {
	    WebDriver driver;

	    @FindBy(className = "complete-header")
	    WebElement successMsg;

	    @FindBy(id = "back-to-products")
	    WebElement backToProductsBtn;

	    public void CheckoutCompletePage(WebDriver driver) {
	        this.driver = driver;
	        PageFactory.initElements(driver, this);
	    }

	    public String getSuccessMessage() {
	        return successMsg.getText();
	    }

	    public void clickBackToProducts() {
	        backToProductsBtn.click();
	    }
	}

