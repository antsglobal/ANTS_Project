package Asset_tracker_post;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class base {

	ExtentHtmlReporter htmlReports;
	public static ExtentReports extent;
	public static ExtentTest parenTtest;
	public static ExtentTest childTest;
	// public static String screenShotPath;

	// Defining extent reports
	@BeforeTest
	public void report() throws UnknownHostException {
		String time_stamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		String rep_name = "MyExtendReports_" + time_stamp + ".html";
		htmlReports = new ExtentHtmlReporter(
				//System.getProperty("user.dir") + "/TestResults/TestReports/" + "MyExtendReports.html");
				System.getProperty("user.dir") + "\\Report\\" + rep_name);
		extent = new ExtentReports();
		extent.attachReporter(htmlReports);

		htmlReports.config().setReportName("Automation_Test_Execution_Report");
		htmlReports.config().setTheme(Theme.STANDARD);

		// htmlReports.config().setTheme(Theme.DARK);

		htmlReports.config().setDocumentTitle("Updated Test Report.");

		// =====================================
		extent.setSystemInfo("Executed Platfom:", "WEB APPLICATION");
		extent.setSystemInfo("Executed By", System.getProperty("user.name"));

		InetAddress myHost = InetAddress.getLocalHost();
		extent.setSystemInfo("Frome Machine: ", myHost.getHostName());
		extent.setSystemInfo("From IP Address: ", myHost.getHostAddress());

		// =====================================
		String css = ".r-img {width: 10%;}";
		htmlReports.config().setCSS(css);

	}

	// Erasing previous data of the report
	@AfterTest
	public void fulshReport() {
		extent.flush();

	}

	// Method name pass as the Test case description
	@BeforeMethod
	public void disp(Method method) {
		String s = method.getName();
		parenTtest = extent.createTest(method.getName());
	}

	// Final test case results will display
	@AfterMethod
	public void setTestResult(ITestResult result) throws IOException {

		if (result.getStatus() == ITestResult.FAILURE) {

			childTest.log(Status.FAIL,
					MarkupHelper.createLabel(result.getName() + " :Test Cases failed due to:", ExtentColor.RED));
			childTest.log(Status.ERROR, result.getThrowable());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			childTest.log(Status.PASS,
					MarkupHelper.createLabel(result.getName() + " :Test Cases is passed:", ExtentColor.GREEN));
		} else if (result.getStatus() == ITestResult.SKIP) {
			childTest.log(Status.SKIP, MarkupHelper.createLabel("Test Cases is Skipped", ExtentColor.ORANGE));
			childTest.log(Status.SKIP, result.getThrowable());
			childTest.log(Status.SKIP, "Test case :" + result.getName() + " is skipped.");
		}

	}

}
