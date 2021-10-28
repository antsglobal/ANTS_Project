package Asset_tracker_post;

import java.io.IOException;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class extentReportDemo {
	
	ExtentHtmlReporter htmlReporter;
	ExtentReports extent;
	
	@BeforeSuite
	void setup() {
		//htmlReporter = new ExtentHtmlReporter("System.getProperty(\"user.dir\")+\"\\\\Report\\\\htmlReport.html");
    	htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir")+"\\Report\\extent_report.html");

        // create ExtentReports and attach reporter(s)
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

	}
	
	@Test
	void test() throws IOException {
		
		ExtentTest test = extent.createTest("MyFirstTest", "Sample description");
		test.log(Status.INFO, "This step shows usage of log(status, details)");
        test.info("This step shows usage of info(details)");        
       
        //String imagePath = System.getProperty("user.dir")+"\\test-output\\screenshot.png";

        
       // test.fail("details", MediaEntityBuilder.createScreenCaptureFromPath(imagePath).build());
        //test.addScreenCaptureFromPath(imagePath);
		
	}
	
	@AfterSuite
	void teardown() {
		extent.flush();
	}

}
