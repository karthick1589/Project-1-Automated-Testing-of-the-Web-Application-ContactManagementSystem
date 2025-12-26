package pageclass;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegistrationPage {
	
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath="//input[@id='firstName']")
	WebElement FirstNameField;
	
	@FindBy(xpath="//input[@id='lastName']")
	WebElement LastNameField;
	
	@FindBy(xpath="//input[@id='email']")
	WebElement EmailField;
	
	@FindBy(xpath="//input[@id='password']")
	WebElement PasswordField;
	
	@FindBy(xpath="//button[@id='submit']")
	WebElement SubmitButton;
	
	@FindBy(xpath="//button[@id='cancel']")
	WebElement CancelButton;
	
	@FindBy(xpath="//span[@id='error']")
    WebElement errorMessage;
	
	public RegistrationPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		PageFactory.initElements(driver, this);
	}
	
	public void enterFirstname(String fname) {
		FirstNameField.clear();
		FirstNameField.sendKeys(fname);
	}
	
	public void enterLastname(String Lname) {
		LastNameField.clear();
		LastNameField.sendKeys(Lname);
	}
	
	public void enterEmail(String Email) {
		EmailField.clear();
		EmailField.sendKeys(Email);
	}
	
	public void enterPassword(String password) {
		PasswordField.clear();
		PasswordField.sendKeys(password);
		
	}
	
	public void registerUser(String fname, String lname, String Email, String password) {
		enterFirstname(fname);
		enterLastname(lname);
		enterEmail(Email);
		enterPassword(password);
	}
	
	public void clickSubmit() {
		wait.until(ExpectedConditions.elementToBeClickable(SubmitButton)).click();
	}
	
	public void clickCancel() {
		CancelButton.click();
	}
	
	public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOf(errorMessage)).getText();
    }
	
	
}
