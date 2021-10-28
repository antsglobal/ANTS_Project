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

public class post_004_assettracking extends base{

	public static Logger log = LogManager.getLogger(post_004_assettracking.class);

	@Test(dataProvider = "assettracking", dataProviderClass = excel_utils.class)
	public void asset_tracking(Map myMapData) {
		
		parenTtest.assignCategory("Webservice : post");
		childTest = parenTtest.createNode("Verifying assettracking webservice ");

		log.info(" ================================================= ");
		log.info(" Asset Tracking : assettracking api test starts");

		RestAssured.baseURI = "http://assettracking.alpha-numero.com:8899/sccl/api/v1";
		RequestSpecification request = RestAssured.given();

		JSONObject request_params = new JSONObject();

		SoftAssert softassert = new SoftAssert();

		int cH_Antenna = Integer.parseInt(myMapData.get("cH_Antenna").toString());

		int sensor_ID = Integer.parseInt(myMapData.get("sensor_ID").toString());

		int subch = Integer.parseInt(myMapData.get("subch").toString());

//		System.out.println(cH_Antenna + "  " + sensor_ID + "  " + subch);

//		System.out.println(myMapData.get("assetCode"));
//		System.out.println(myMapData.get("latitude"));
//		System.out.println(myMapData.get("longitude"));
//		System.out.println(myMapData.get("rFIDReaderCode"));
//		System.out.println(myMapData.get("time_stamp"));

		request_params.put("assetCode", myMapData.get("assetCode").toString());
		request_params.put("cH_Antenna", cH_Antenna); // 106 - 21 101 - 17 ( always showing success )
		request_params.put("latitude", myMapData.get("latitude").toString());
		request_params.put("longitude", myMapData.get("longitude").toString());
		request_params.put("rFIDReaderCode", myMapData.get("rFIDReaderCode").toString());
		request_params.put("sensor_ID", sensor_ID);
		request_params.put("subch", subch);
		request_params.put("time_stamp", myMapData.get("time_stamp").toString());

		request.header("Content-type", "application/json");
		request.body(request_params.toJSONString());

		// Response responce = httpRequest.request(Method.POST,"/assetadding");

		Response response = request.post("/assettracking");

		int statusCode = response.getStatusCode();
		System.out.println("The status code recieved: " + statusCode);

		try {

			Assert.assertEquals(statusCode, 200);

			log.info(" Successful response code received ");
			childTest.log(Status.INFO, " Successful response code received ");

			// softassert.assertEquals(statusCode, 200, "Status code mismatch");

			// System.out.println("Response body: " + response.getBody().asString());
//
//			System.out.println("==============================================");
//			System.out.println("Response body: " + response.jsonPath().prettify());

			// Fetch status of the response
			String status = response.jsonPath().getString("status");
			System.out.println(" Response status			: " + status);

			// Fetch status message
			String message = response.jsonPath().getString("message");
			System.out.println(" Response status message		: " + message);

			try {

				Assert.assertEquals(message, "Success");
				log.info(" response message as : " + message);
				log.info(" Test case passed ");
				childTest.log(Status.INFO, " response message as : " + message);
				childTest.log(Status.INFO, " Test case passed ");

			} catch (AssertionError | Exception e) {
				log.info("Test fail: " + e);
				childTest.log(Status.INFO, "Test case fail : Response message mismatch ");
				// log.info("Test case fail : Response message mismatch");
				softassert.fail("Test case fail : Response message mismatch ");
			}

		} catch (AssertionError e) {

			//softassert.assertEquals(statusCode, 200, "Status code mismatch");
			//log.info(" Test Fail : Successful response code not received");
			log.info(" Test Fail : " + e);
			childTest.log(Status.INFO, "Test Fail : Unsuccessful Response code received");
			softassert.fail( "Test Fail : Unsuccessful Response code received");
			
			
		}

		softassert.assertAll();

	}

}
