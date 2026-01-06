package testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import Baseclass.BaseTest;
import pageclass.AddContactPage;
import pageclass.ContactDetailsPage;
import pageclass.ContactListPage;
import pageclass.LoginPage;

@Listeners(Baseclass.TestListener.class)

public class EditContactTest extends BaseTest {
	LoginPage loginPage;
	ContactListPage contactListPage;
	ContactDetailsPage contactDetailsPage;
	AddContactPage editPage;

	String FirstName;
	String LastName = "EditTest";

	@BeforeMethod
	public void setup() {

		loginPage = new LoginPage(getDriver());
		contactListPage = new ContactListPage(getDriver());

		loginPage.login("test@set.com", "test@123$");
	}

	@Test(priority = 1)
    public void testEditContactSuccessfully() {
        test.set(report.createTest("Testcase-Edit Contact Functionality-4.16- Verify user can edit an existing contact"));

        try {

            getTest().log(Status.INFO, "Selecting contact.");
            contactDetailsPage = contactListPage.clickFirstContact();

            getTest().log(Status.INFO, "Clicking Edit.");
            editPage = contactDetailsPage.clickEditContact();

            String newEmail = "update" + System.currentTimeMillis() + "@test.com";
            String newPhone = "9876543210";

            getTest().log(Status.INFO, "Updating Email to: " + newEmail);
            editPage.updateEmail(newEmail);
            
            getTest().log(Status.INFO, "Updating Phone to: " + newPhone);
            editPage.updatePhone(newPhone);

            getTest().log(Status.INFO, "Clicking Submit.");
            editPage.clickSubmit();

            getTest().log(Status.INFO, "Waiting for browser to display new email...");
            contactDetailsPage.waitForUpdatedEmail(newEmail);

            getTest().log(Status.INFO, "Verifying updated details on Details Page...");
            
            String displayedEmail = contactDetailsPage.getEmail();
            String displayedPhone = contactDetailsPage.getPhoneNumber();

            Assert.assertEquals(displayedEmail, newEmail, "Email update failed!");
            Assert.assertEquals(displayedPhone, newPhone, "Phone update failed!");

            getTest().log(Status.PASS, "Contact verified successfully.");

        } catch (AssertionError e) {
        	getTest().log(Status.FAIL, "Assertion Failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
        	getTest().log(Status.FAIL, "Exception Occurred: " + e.getMessage());
            throw e;
        }
    }

	@Test(priority = 2)
	public void testCancelEdit() {
		test.set(report.createTest("Testcase-Edit Contact Functionality-4.17- Verify Cancel an Edit"));

		try {
			contactDetailsPage = contactListPage.clickFirstContact();
			editPage = contactDetailsPage.clickEditContact();

			String originalPhone = "0000000000";
			getTest().log(Status.INFO, "Typing new phone but clicking Cancel.");
			editPage.updatePhone(originalPhone);
			editPage.clickCancel();

			String displayedPhone = contactDetailsPage.getPhoneNumber();

			Assert.assertNotEquals(displayedPhone, originalPhone, "Phone should not have changed after cancel!");
			getTest().log(Status.PASS, "Cancel button worked, data was not modified.");

		} catch (Exception e) {
			getTest().log(Status.FAIL, "Cancel Test Failed: " + e.getMessage());
			throw e;
		}
	}
	
	@Test(priority = 3)
    public void testEditValidationMissingField() {
        test.set(report.createTest("Testcase-Edit Contact Functionality-4.18- Verify validation during edit"));

        try {

        	getTest().log(Status.INFO, "Selecting a contact to edit.");
            contactDetailsPage = contactListPage.clickFirstContact();

            getTest().log(Status.INFO, "Clicking Edit button.");
            editPage = contactDetailsPage.clickEditContact();

            getTest().log(Status.INFO, "Clearing the 'Last Name' field.");
            editPage.clearLastName();

            getTest().log(Status.INFO, "Clicking Submit (expecting failure).");
            editPage.clickSubmit();

            String actualError = editPage.getErrorMessage();
            getTest().log(Status.INFO, "Error Message Captured: " + actualError);

            Assert.assertTrue(actualError.contains("lastName") && actualError.contains("required"),
                "Error message did not contain expected text. Found: " + actualError);

            getTest().log(Status.PASS, "Validation error verified successfully.");

        } catch (AssertionError e) {
        	getTest().log(Status.FAIL, "Assertion Failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
        	getTest().log(Status.FAIL, "Test Failed due to exception: " + e.getMessage());
            throw e;
        }
    }
}
