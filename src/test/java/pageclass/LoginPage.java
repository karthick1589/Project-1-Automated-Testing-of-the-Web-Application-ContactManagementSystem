package pageclass;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
	
	WebDriver driver;
    WebDriverWait wait;
    
    @FindBy(xpath="//input[@id='email']")
    WebElement emailField;
    @FindBy(xpath="//input[@id='password']")
    WebElement passwordField;
    @FindBy(id="submit")
    WebElement submit;
    @FindBy(id="error")
    WebElement Errormsg;
    @FindBy(id="signup")
    WebElement SignupButton;
    @FindBy(xpath="//button[@id='logout']")
    WebElement LogoutButton;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void enterEmail(String email) {
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickLogin() {
        submit.click();
    }
    
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }
    
    public void logout() {
    	if(LogoutButton.isDisplayed()) {
    		LogoutButton.click();	
    	}
    	
    }

    public String getGlobalErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOf(Errormsg)).getText();
    }

    public String getPasswordFieldType() {
        return passwordField.getAttribute("type");
    }
    
	public RegistrationPage clickSignUp() {
        SignupButton.click();
        return new RegistrationPage(driver);
	}


}
