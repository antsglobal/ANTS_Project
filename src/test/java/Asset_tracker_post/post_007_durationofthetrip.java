package Asset_tracker_post;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.excel_utils;

public class post_007_durationofthetrip extends base{
	

	public static Logger log = LogManager.getLogger(post_007_durationofthetrip.class);

	@Test(dataProvider = "p_durationofthetrip", dataProviderClass = excel_utils.class)
	public void dumperdetailscount(Map myMapData) {
		
		parenTtest.assignCategory("Webservice : post");
		childTest = parenTtest.createNode("Verifying durationofthetrip webservice ");

		log.info(" ================================================= ");
		log.info(" Asset Tracking : durationofthetrip api test starts");

		RestAssured.baseURI = "http://assettracking.alpha-numero.com:8899/sccl/api/v1";
		RequestSpecification request = RestAssured.given();

		JSONObject request_params = new JSONObject();

		SoftAssert softassert = new SoftAssert();

		request_params.put("fromDate", myMapData.get("fromDate").toString());
		request_params.put("toDate", myMapData.get("toDate").toString());

		request.header("Content-type", "application/json");
		request.body(request_params.toJSONString());

		Response response = request.post("/durationofthetrip");

		int statusCode = response.getStatusCode();

		//System.out.println(response.body().prettyPrint());
		System.out.println("The status code recieved: " + statusCode);

		try {

			Assert.assertEquals(statusCode, 200);

			log.info(" Successful response code received ");
			childTest.log(Status.INFO, " Successful response code received ");

			// Fetch status of the response
			String res_status = response.jsonPath().getString("status");
			System.out.println(" Response status			: " + res_status);

			// Fetch status message
			String message = response.jsonPath().getString("message").trim();
			System.out.println(" Response status message		: " + message);
			
			try {
				
				Assert.assertEquals(message, "Duration of Trip details fetch successfully success");
				log.info("Response message matched");
				log.info("Test case pass");
				childTest.log(Status.INFO, "Response message matched");
				childTest.log(Status.INFO, "Test case pass");
				
			} catch(AssertionError | Exception e) {
				log.info("Test fail: " + e);
				childTest.log(Status.INFO, "Test case fail : Response message mismatch ");
				//log.info("Test case fail : Response message mismatch");
				softassert.fail("Test case fail : Response message mismatch ");
				//softassert.assertEquals(message, "Duration of Trip details fetch successfully success1", "Response message mismatch");
			}

		} catch(AssertionError | Exception e) {
			log.info("Test fail: " + e);
			childTest.log(Status.INFO, "Test Fail : Unsuccessful Response code received");
			//log.info("Test case fail : Unsuccessful Response received");
			softassert.fail("Test Fail : Unsuccessful Response code received" );
		}
		
		softassert.assertAll();

	}



}
