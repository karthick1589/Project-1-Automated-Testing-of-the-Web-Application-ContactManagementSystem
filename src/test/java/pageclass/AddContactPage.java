package pageclass;

import java.time.Duration;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddContactPage {

	WebDriver driver;
	WebDriverWait wait;

	@FindBy(xpath = "//button[@id='add-contact']") WebElement addnewcontact;
	@FindBy(xpath = "//input[@id='firstName']") WebElement firstName;
	@FindBy(xpath = "//input[@id='lastName']") WebElement lastName;
	@FindBy(xpath = "//input[@id='birthdate']") WebElement birthdate;
	@FindBy(xpath = "//input[@id='email']") WebElement email;
	@FindBy(xpath = "//input[@id='phone']") WebElement phone;
	@FindBy(xpath = "//input[@id='street1']") WebElement street1;
	@FindBy(xpath = "//input[@id='street2']") WebElement street2;
	@FindBy(xpath = "//input[@id='city']") WebElement city;
	@FindBy(xpath = "//input[@id='stateProvince']") WebElement state;
	@FindBy(xpath = "//input[@id='postalCode']") WebElement postalCode;
	@FindBy(xpath = "//input[@id='country']") WebElement country;

	@FindBy(xpath = "//button[@id='submit']") WebElement submitBtn;
	@FindBy(xpath = "//button[@id='cancel']") WebElement cancelBtn;
	@FindBy(id = "error") WebElement errorMessage;

	public AddContactPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}

	public void fillContactForm(String fname, String lname, String dob, String emailVal, String phoneVal, 
			String street, String cityVal, String stateVal, String postal, String countryVal) {
		if(fname != null) firstName.sendKeys(fname);
		if(lname != null) lastName.sendKeys(lname);
		if(dob != null) birthdate.sendKeys(dob);
		if(emailVal != null) email.sendKeys(emailVal);
		if(phoneVal != null) phone.sendKeys(phoneVal);
		if(street != null) street1.sendKeys(street);
		if(cityVal != null) city.sendKeys(cityVal);
		if(stateVal != null) state.sendKeys(stateVal);
		if(postal != null) postalCode.sendKeys(postal);
		if(countryVal != null) country.sendKeys(countryVal);
	}

	public void clickSubmit() {
		wait.until(ExpectedConditions.elementToBeClickable(submitBtn)).click();
	}

	public String getErrorMessage() {
		return wait.until(ExpectedConditions.visibilityOf(errorMessage)).getText();
	}

	public boolean isFirstNameEmpty() {
		return firstName.getAttribute("value").isEmpty();
	}
	
	public void clickCancel() {
	    wait.until(ExpectedConditions.elementToBeClickable(cancelBtn)).click();
	}

	public void clearLastName() {
		robustClear(lastName);
	}

	public void updateEmail(String newEmail) {
        robustClear(email);
        email.sendKeys(newEmail);
    }

    public void updatePhone(String newPhone) {
        robustClear(phone);
        phone.sendKeys(newPhone);
    }

    private void robustClear(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        
        element.click();

        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        
        element.sendKeys(Keys.BACK_SPACE);

        try { Thread.sleep(500); } catch (InterruptedException e) {}
    }

}
