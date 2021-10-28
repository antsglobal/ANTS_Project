package Asset_tracker_post;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
 
import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;


public class base1 {
 
	//WebDriver driver;
	ExtentHtmlReporter htmlReports;
	ExtentReports extent;
	ExtentTest test;
	ExtentTest childTest;
 
	@BeforeTest
	public void reportsetUp() throws UnknownHostException {
		htmlReports = new ExtentHtmlReporter(
				System.getProperty("user.dir") + "\\Report\\" + "myreport5.html");
		extent = new ExtentReports();
		extent.attachReporter(htmlReports);
		extent.setAnalysisStrategy(AnalysisStrategy.TEST);
		test = extent.createTest("Asset Tracking");
		htmlReports.config().setReportName("Automation_Test_Execution_Report");
		htmlReports.config().setTheme(Theme.STANDARD);
		htmlReports.config().setDocumentTitle("Latest Test Report.");
 
		extent.setSystemInfo("Executed Platfom:", "WEB Services");
		//extent.setSystemInfo("Executed By", utilities.getUserName());
		//extent.setSystemInfo("Frome Machine: ", utilities.getHostName());
		//extent.setSystemInfo("From IP Address: ", utilities.getIpAddress());
	}
 
	@AfterTest
	public void fulshReport() {
		extent.flush();
	}
 
	
	
 
	@AfterMethod
	public void setTestResult(ITestResult result) throws IOException {
		
		if (result.getStatus() == ITestResult.FAILURE) {
			
			childTest.log(Status.FAIL,
					MarkupHelper.createLabel("Test Cases is failed due to:" + result.getName(), ExtentColor.RED));
			childTest.log(Status.ERROR, result.getThrowable());
			
			//String screenshot = baseSE.getScreenShot1(driver, result);
			
			//childTest.fail("test case failed " ,
					//MediaEntityBuilder.createScreenCaptureFromPath(screenshot).build());
			
			//childTest.log(Status.FAIL, "Screenshot captured" + childTest.addScreenCaptureFromPath(screenshot));
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			
			childTest.log(Status.PASS,
					MarkupHelper.createLabel("Test Cases is passed:" + result.getName(), ExtentColor.GREEN));
			
		} else if (result.getStatus() == ITestResult.SKIP) {
			childTest.log(Status.SKIP, MarkupHelper.createLabel("Test Cases is Skipped", ExtentColor.ORANGE));
			childTest.log(Status.SKIP, result.getThrowable());
			childTest.log(Status.SKIP, "Test case :" + result.getName() + " is skipped.");
		}
 
		//driver.quit();
	}
 
	
 }