package testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import Baseclass.BaseTest;
import pageclass.AddContactPage;
import pageclass.ContactListPage;
import pageclass.LoginPage;
@Listeners(Baseclass.TestListener.class)

public class AddContactTest extends BaseTest {

	LoginPage loginPage;
	ContactListPage contactListPage;
	AddContactPage addContactPage;

	@BeforeMethod
	public void setupAndLogin() {
		loginPage = new LoginPage(getDriver());
		loginPage.login("test@set.com", "test@123$");
		contactListPage = new ContactListPage(getDriver());
	}

	@Test(priority = 1)
	public void testAddValidContact() {

		test.set(report.createTest("Testcase: Add Contact Functionality-3.11- Verify adding contact with all valid details"));
		addContactPage = contactListPage.clickAddContact();

		String fname = "Kadamba" + System.currentTimeMillis();
		String lname = "vel";

		addContactPage.fillContactForm(fname, lname, "1990-01-01", "kadamba@test.com", "1234567890", "123 Street",
				"chennai", "Tamilnadu", "600100", "India");
		addContactPage.clickSubmit();
		try {
			Assert.assertTrue(contactListPage.isContactDisplayed(fname, lname),
					"Contact was not found in the list after adding.");
			getTest().log(Status.PASS, "Contact Added successfully.");
		} catch (AssertionError e) {
			getTest().log(Status.FAIL, "Assertion Failed: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			getTest().log(Status.FAIL, "Exception Occurred: " + e.getMessage());
			throw e;
		}

	}

	@Test(priority = 2)
	public void testAddContactMissingRequiredFields() {

		test.set(report.createTest(
				"Testcase: Add Contact Functionality-3.12- Verify adding contact with missing required fields"));

		addContactPage = contactListPage.clickAddContact();

		addContactPage.fillContactForm(null, null, "1990-01-01", "test@test.com", "1234567890", null, null, null, null,
				null);
		addContactPage.clickSubmit();

		String error = addContactPage.getErrorMessage();
		try {
			Assert.assertTrue(error.contains("firstName") && error.contains("required"),
					"Expected error to complain about missing firstName, but got: " + error);
			Assert.assertTrue(error.contains("lastName") && error.contains("required"),
					"Expected error to complain about missing lastName, but got: " + error);
			getTest().log(Status.PASS, "browser shown " + error + " successfully");
		} catch (AssertionError e) {
			getTest().log(Status.FAIL, "Assertion Failed: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			getTest().log(Status.FAIL, "Exception Occurred: " + e.getMessage());
			throw e;
		}

	}

	@Test(priority = 3)
	public void testInvalidPhoneInput() {

		test.set(report.createTest("Testcase: Add Contact Functionality-3.13- Verify phone field accepts only numeric input"));

		addContactPage = contactListPage.clickAddContact();

		addContactPage.fillContactForm("Alice", "Smith", null, null, "INVALIDPHONE", null, null, null, null, null);
		addContactPage.clickSubmit();

		String error = addContactPage.getErrorMessage();
		try {
			Assert.assertTrue(error.contains("Validation failed") || error.contains("Phone"),
					"Expected error for invalid phone format, but got: " + error);
			getTest().log(Status.PASS, "Expected " + error + " shown successfully");
		} catch (AssertionError e) {
			getTest().log(Status.FAIL, "Assertion Failed: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			getTest().log(Status.FAIL, "Exception Occurred: " + e.getMessage());
			throw e;
		}

	}

	@Test(priority = 4)
	public void testDuplicateContactBehavior() {

		test.set(report.createTest("Testcase: Add Contact Functionality-3.14- Verify adding duplicate contact details"));

		String fname = "Duplicate";
		String lname = "User";

		addContactPage = contactListPage.clickAddContact();
		addContactPage.fillContactForm(fname, lname, "1990-01-01", null, null, null, null, null, null, null);
		addContactPage.clickSubmit();

		addContactPage = contactListPage.clickAddContact();
		addContactPage.fillContactForm(fname, lname, "1990-01-01", null, null, null, null, null, null, null);
		addContactPage.clickSubmit();

		int count = contactListPage.getContactCount(fname);
		try {
			Assert.assertTrue(count >= 2, "Duplicate contact was not added (Count should be >= 2)");
			getTest().log(Status.PASS, "duplicate contact details added and verified successfully");
		} catch (AssertionError e) {
			getTest().log(Status.FAIL, "Assertion Failed: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			getTest().log(Status.FAIL, "Exception Occurred: " + e.getMessage());
			throw e;
		}

	}

	@Test(priority = 5)
	public void testFormResetAfterAddition() {

		test.set(report.createTest("Testcase: Add Contact Functionality-3.15- Verify form resets after contact is added"));

		addContactPage = contactListPage.clickAddContact();
		addContactPage.fillContactForm("Reset", "Test", "1990-01-01", null, null, null, null, null, null, null);
		addContactPage.clickSubmit();

		addContactPage = contactListPage.clickAddContact();
		try {
			Assert.assertTrue(addContactPage.isFirstNameEmpty(), "Form did not reset; First Name field is not empty.");
			getTest().log(Status.PASS, "Verified the form resets, after contact is added");
		} catch (AssertionError e) {
			getTest().log(Status.FAIL, "Assertion Failed: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			getTest().log(Status.FAIL, "Exception Occurred: " + e.getMessage());
			throw e;
		}
	}

}
