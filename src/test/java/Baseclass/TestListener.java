package Baseclass;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

public class TestListener implements ITestListener { 

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test Failed: " + result.getName());
        
        Object testInstance = result.getInstance();
        BaseTest baseTest = (BaseTest) testInstance;
        
        BaseTest.getTest().log(Status.FAIL, "Test Failed: " + result.getThrowable().getMessage());
        
        String screenshotPath;
		try {
			screenshotPath = baseTest.takeScreenshot(result.getMethod().getMethodName());
	        BaseTest.getTest().fail(
	    		    "Screenshot on Failure:", 
	    		    MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
    public void onTestStart(ITestResult result) {

    }

    @Override
    public void onTestSuccess(ITestResult result) {
    	System.out.println("Test Passed: " + result.getName());

        Object testInstance = result.getInstance();
        BaseTest baseTest = (BaseTest) testInstance;

        BaseTest.getTest().log(Status.PASS, "Test Passed Successfully");

        String screenshotPath;
		try {
			screenshotPath = baseTest.takeScreenshot(result.getMethod().getMethodName());
	        BaseTest.getTest().pass(
	                "Screenshot on Success:", 
	                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build()
	            );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    @Override
    public void onTestSkipped(ITestResult result) {}
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override public void onStart(ITestContext context) {}
    @Override public void onFinish(ITestContext context) {}
}