
	package pages;

	import org.openqa.selenium.By;
	import org.openqa.selenium.WebDriver;

	public class LoginPage {

	    WebDriver driver;

	    // Locators
	    private By username = By.id("user-name");
	    private By password = By.id("password");
	    private By loginButton = By.id("login-button");

	    // Constructor
	    public LoginPage(WebDriver driver) {
	        this.driver = driver;
	    }

	    // Actions
	    public void enterUsername(String user) {
	        driver.findElement(username).sendKeys(user);
	    }

	    public void enterPassword(String pass) {
	        driver.findElement(password).sendKeys(pass);
	    }

	    public void clickLogin() {
	        driver.findElement(loginButton).click();
	    }

	    public void login(String user, String pass) {
	        enterUsername(user);
	        enterPassword(pass);
	        clickLogin();
	    }
	}

