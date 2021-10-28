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

public class post_006_dumperdetailscount extends base{

	public static Logger log = LogManager.getLogger(post_006_dumperdetailscount.class);

	@Test(dataProvider = "p_dumperdetailscount", dataProviderClass = excel_utils.class)
	public void dumperdetailscount(Map myMapData) {
		
		parenTtest.assignCategory("Webservice : post");
		childTest = parenTtest.createNode("Verifying dumperdetailscount webservice ");

		log.info(" ================================================= ");
		log.info(" Asset Tracking : dumperdetailscount api test starts");

		RestAssured.baseURI = "http://assettracking.alpha-numero.com:8899/sccl/api/v1";
		RequestSpecification request = RestAssured.given();

		JSONObject request_params = new JSONObject();

		SoftAssert softassert = new SoftAssert();

		request_params.put("fromDate", myMapData.get("fromDate").toString());
		request_params.put("toDate", myMapData.get("toDate").toString());

		request.header("Content-type", "application/json");
		request.body(request_params.toJSONString());

		Response response = request.post("/dumperdetailscount");

		int statusCode = response.getStatusCode();

//		System.out.println(response.jsonPath().prettify());

		System.out.println("The status code recieved: " + statusCode);

		try {

			Assert.assertEquals(statusCode, 200);

			log.info(" Successful response code received ");
			childTest.log(Status.INFO, " Successful response code received ");

			// softassert.assertEquals(statusCode, 200, "Status code mismatch");

			// System.out.println("Response body: " + response.getBody().asString());

			//System.out.println("==============================================");
			//System.out.println("Response body: " + response.jsonPath().prettify());

			// Fetch status of the response
			String res_status = response.jsonPath().getString("status");
			System.out.println(" Response status			: " + res_status);

			// Fetch status message
			String message = response.jsonPath().getString("message").trim();
			System.out.println(" Response status message		: " + message);
			
			try {
				
				Assert.assertEquals(message, "Dumper details count fetch successfully");
				log.info("Response message matched");
				log.info("Test case pass");
				childTest.log(Status.INFO, "Response message matched");
				childTest.log(Status.INFO, "Test case pass");
				
			} catch(AssertionError | Exception e) {
				log.info("Test fail: " + e);
				childTest.log(Status.INFO, "Test case fail : Response message mismatch ");
				//log.info("Test case fail : Response message mismatch");
				softassert.fail("Test case fail : Response message mismatch ");
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
