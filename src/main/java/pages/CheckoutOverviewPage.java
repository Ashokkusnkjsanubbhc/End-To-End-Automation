
	package pages;

	import org.openqa.selenium.*;
	import org.openqa.selenium.support.*;

	public class CheckoutOverviewPage {
	    WebDriver driver;

	    @FindBy(className = "summary_total_label")
	    WebElement totalLabel;

	    @FindBy(id = "finish")
	    WebElement finishBtn;

	    public CheckoutOverviewPage(WebDriver driver) {
	        this.driver = driver;
	        PageFactory.initElements(driver, this);
	    }

	    public String getTotalAmount() {
	        return totalLabel.getText();
	    }

	    public void clickFinish() {
	        finishBtn.click();
	    }
	}

