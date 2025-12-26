package pageclass;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ContactListPage {

	WebDriver driver;
	WebDriverWait wait;

	@FindBy(xpath = "//button[@id='add-contact']")
	WebElement addContactBtn;
	@FindBy(className = "contactTable")
	WebElement contactTable;
	// @FindBy(className = "contactTableBodyRow") List<WebElement> tableRows;

	public ContactListPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}

	public AddContactPage clickAddContact() {
		wait.until(ExpectedConditions.elementToBeClickable(addContactBtn)).click();
		return new AddContactPage(driver);
	}

	public ContactDetailsPage clickFirstContact() {
		try {
			WebElement firstContact = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[@class='contactTableBodyRow'][1]")));

			firstContact.click();
			return new ContactDetailsPage(driver);

		} catch (TimeoutException e) {
			throw new RuntimeException("Contact List is likely empty! Cannot perform Edit Test.", e);
		}
	}

	public ContactDetailsPage clickContactByIndex(int index) {

		List<WebElement> rows = wait
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("contactTableBodyRow")));

		if (index < 0 || index >= rows.size()) {
			throw new RuntimeException("Invalid Index: " + index + ". Total contacts: " + rows.size());
		}
		rows.get(index).click();
		return new ContactDetailsPage(driver);
	}

	public boolean isListEmpty() {
		
        return driver.findElements(By.className("contactTableBodyRow")).isEmpty();
    }

	public boolean isContactDisplayed(String firstName, String lastName) {

		String dynamicXpath = "//td[contains(text(), '" + firstName + "')]";

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXpath)));
		return true;
	}

	public int getContactCount(String firstName) {
		List<WebElement> contacts = driver.findElements(By.xpath("//td[contains(text(), '" + firstName + "')]"));
		return contacts.size();
	}

	public ContactListPage clickOnContact(String firstName) {
		WebElement contactLink = wait.until(
				ExpectedConditions.elementToBeClickable(By.xpath("//td[contains(text(), '" + firstName + "')]")));
		contactLink.click();
		return new ContactListPage(driver);
	}

	public String getPhoneNumber() {
		return null;
	}

	public ContactDetailsPage clickContactByName(String fullName) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		String xpath = "//td[normalize-space()='" + fullName + "']";
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
		return new ContactDetailsPage(driver);
	}
	
	public boolean isContactPresent(String contactName) {
	    List<WebElement> matches = driver.findElements(By.xpath("//td[contains(text(), '" + contactName + "')]"));
	    return !matches.isEmpty();
	}

}
