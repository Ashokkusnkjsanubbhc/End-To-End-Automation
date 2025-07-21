
	package pages;

	import org.openqa.selenium.*;
	import org.openqa.selenium.support.*;

	public class CheckoutPage {
	    WebDriver driver;

	    @FindBy(id = "first-name")
	    WebElement firstNameInput;

	    @FindBy(id = "last-name")
	    WebElement lastNameInput;

	    @FindBy(id = "postal-code")
	    WebElement postalCodeInput;

	    @FindBy(id = "continue")
	    WebElement continueBtn;

	    @FindBy(css = "[data-test='error']")
	    WebElement errorMsg;

	    public CheckoutPage(WebDriver driver) {
	        this.driver = driver;
	        PageFactory.initElements(driver, this);
	    }

	    public void enterCheckoutInfo(String first, String last, String zip) {
	        firstNameInput.sendKeys(first);
	        lastNameInput.sendKeys(last);
	        postalCodeInput.sendKeys(zip);
	        continueBtn.click();
	    }

	    public String getErrorMessage() {
	        return errorMsg.getText();
	    }
	
}
