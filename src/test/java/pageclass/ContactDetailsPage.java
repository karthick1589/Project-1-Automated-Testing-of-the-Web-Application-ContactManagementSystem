package pageclass;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ContactDetailsPage {

    WebDriver driver;
    WebDriverWait wait;

    @FindBy(id = "edit-contact") WebElement editButton;
    @FindBy(id = "delete") WebElement deleteButton;
    @FindBy(id = "return") WebElement returnButton;

    @FindBy(id = "firstName") WebElement firstNameVal;
    @FindBy(id = "lastName") WebElement lastNameVal;
    @FindBy(id = "phone") WebElement phoneVal;
    @FindBy(id = "email") WebElement emailVal;

    public ContactDetailsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public AddContactPage clickEditContact() {
        wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();
        return new AddContactPage(driver); 
    }

    public void clickReturnToContactList() {
        wait.until(ExpectedConditions.elementToBeClickable(returnButton)).click();
    }
    
    public void waitForUpdatedEmail(String expectedEmail) {

        wait.until(ExpectedConditions.visibilityOf(editButton));
        
        wait.until(ExpectedConditions.textToBePresentInElement(emailVal, expectedEmail));
    }

    public String getPhoneNumber() {
        WebElement el = wait.until(ExpectedConditions.visibilityOf(phoneVal));
        String text = el.getText();
        if(text == null || text.isEmpty()) {
            return el.getAttribute("value");
        }
        return text;
    }

    public String getEmail() {
        WebElement el = wait.until(ExpectedConditions.visibilityOf(emailVal));
        String text = el.getText();
        if(text == null || text.isEmpty()) {
            return el.getAttribute("value");
        }
        return text;
    }

    public String getFirstName() {
        WebElement el = wait.until(ExpectedConditions.visibilityOf(firstNameVal));
        String text = el.getText();
        if(text == null || text.isEmpty()) {
            return el.getAttribute("value");
        }
        return text;
    }
      
    public void clickDeleteContact() {
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
    }
    
}