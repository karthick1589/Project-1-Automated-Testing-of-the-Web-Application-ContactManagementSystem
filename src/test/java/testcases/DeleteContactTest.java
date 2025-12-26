package testcases;

import java.time.Duration;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import Baseclass.BaseTest;
import pageclass.AddContactPage;
import pageclass.ContactDetailsPage;
import pageclass.ContactListPage;
import pageclass.LoginPage;

public class DeleteContactTest extends BaseTest {

	LoginPage loginPage;
	ContactListPage contactListPage;
	ContactDetailsPage contactDetailsPage;
	AddContactPage editPage;

	@BeforeMethod
	public void setup() {

		loginPage = new LoginPage(driver);
		loginPage.login("test@set.com", "test@123$");
		contactListPage = new ContactListPage(driver);

		if (contactListPage.isListEmpty()) {
			System.out.println("DEBUG: List is empty. Creating a contact for Delete Test...");

			AddContactPage addPage = contactListPage.clickAddContact();
			addPage.fillContactForm("Delete", "Me", "1990-01-01", "delete@test.com", "1234567890", "Street", "City",
					"State", "12345", "USA");
			addPage.clickSubmit();

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.urlContains("contactList"));

			contactListPage = new ContactListPage(driver);
		}
	}

	@Test(priority = 1)
	public void testSelectSpecificContact() {

		test = report.createTest("Testcase-Edit Contact Functionality-5.19- Verify deleting a contact");

		contactDetailsPage = contactListPage.clickContactByIndex(0);

		contactDetailsPage.clickDeleteContact();

		driver.switchTo().alert().accept();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlContains("contactList"));

		try {
			Assert.assertTrue(driver.getCurrentUrl().contains("contactList"),
					"Failed to return to Contact List after deletion.");
			test.log(Status.PASS, " Contact is removed from the list successfully");
		} catch (AssertionError e) {
			test.log(Status.FAIL, "Assertion Failed: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception Occurred: " + e.getMessage());
			throw e;
		}
	}

	@Test(priority = 2)
	public void testDeleteContactAlert() {
		test = report.createTest("Testcase-Edit Contact Functionality-5.20- Verify delete confirmation popup appears");

		try {
			test.log(Status.INFO, "Selecting a contact to delete.");
			contactDetailsPage = contactListPage.clickFirstContact();

			test.log(Status.INFO, "Clicking the Delete button.");
			contactDetailsPage.clickDeleteContact();

			test.log(Status.INFO, "Waiting for browser alert to appear...");

			org.openqa.selenium.Alert alert = new WebDriverWait(driver, java.time.Duration.ofSeconds(5))
					.until(ExpectedConditions.alertIsPresent());

			String alertText = alert.getText();
			test.log(Status.INFO, "Alert Text Captured: " + alertText);

			String expectedText = "Are you sure you want to delete this contact?";
			Assert.assertEquals(alertText, expectedText, "Alert text did not match!");

			alert.dismiss();

			test.log(Status.PASS, "Delete confirmation popup verified and dismissed successfully.");

			Assert.assertTrue(driver.getCurrentUrl().contains("contactDetails"),
					"Should remain on details page after cancel.");

		} catch (AssertionError e) {
			test.log(Status.FAIL, "Assertion Failed: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			test.log(Status.FAIL, "Delete Alert Test Failed: " + e.getMessage());
			throw e;
		}
	}
	
	@Test(priority = 5)
    public void testDeleteContactPermanently() {
        test = report.createTest("Testcase-Edit Contact Functionality-5.21- Verify contact is removed after page refresh");

        try {

            test.log(Status.INFO, "Selecting a contact to delete.");
            contactDetailsPage = contactListPage.clickFirstContact();

            String contactName = contactDetailsPage.getFirstName(); 
            
            test.log(Status.INFO, "Targeting contact for deletion: " + contactName);

            contactDetailsPage.clickDeleteContact();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.alertIsPresent()).accept();
            test.log(Status.INFO, "Accepted delete confirmation alert.");

            wait.until(ExpectedConditions.urlContains("contactList"));
            
            test.log(Status.INFO, "Refreshing the page to ensure deletion is persistent.");
            driver.navigate().refresh();
            
            boolean isFound = contactListPage.isContactPresent(contactName);

            if (!isFound) {
                test.log(Status.PASS, "Success: Contact '" + contactName + "' is no longer found in the list.");
            } else {
                test.log(Status.FAIL, "Failure: Contact '" + contactName + "' is STILL present after deletion!");
                Assert.fail("Contact was not deleted successfully.");
            }

        } catch (Exception e) {
            test.log(Status.FAIL, "Delete Verification Failed: " + e.getMessage());
            throw e;
        }
    }
}
