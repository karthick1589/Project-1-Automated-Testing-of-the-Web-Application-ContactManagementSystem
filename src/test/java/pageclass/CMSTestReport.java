package pageclass;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class CMSTestReport {

	public static void main(String[] args) {
		
		ExtentSparkReporter html = new ExtentSparkReporter(System.getProperty("user.dir") + "/CMSTestReport.html");
		ExtentReports report = new ExtentReports();
		report.attachReporter(html);
		ExtentTest test = report.createTest("Testcase-1-Login Functionality");
		
		test.log(Status.FAIL, "Failed login with valid credentials");
		test.log(Status.PASS, "Passed login with valid credentials");
		test.log(Status.INFO, "INFORMATION");
		
				

	}

}
