package Asset_tracker_post;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.excel_utils;

public class post_002_AssetAdd extends base{

	public static Logger log = LogManager.getLogger(post_002_AssetAdd.class);

	@Test(dataProvider = "assetadding", dataProviderClass = excel_utils.class)
	public void assetAdd(Map myMapData) {
		
		parenTtest.assignCategory("Webservice : post");
		childTest = parenTtest.createNode("Verifying assetadding webservice ");

		log.info(" ================================================= ");
		log.info(" Asset Tracking : assetadding api test starts");

		RestAssured.baseURI = "http://assettracking.alpha-numero.com:8899/sccl/api/v1";
		RequestSpecification request = RestAssured.given();

		JSONObject request_params = new JSONObject();

		SoftAssert softassert = new SoftAssert();

		int locationId = Integer.parseInt(myMapData.get("locationId").toString());
		int status = Integer.parseInt(myMapData.get("status").toString());

		request_params.put("assetCode", myMapData.get("assetCode").toString());
		request_params.put("assetDescription", myMapData.get("assetDescription").toString());
		request_params.put("locationId", locationId);
		request_params.put("status", status);

		request.header("Content-type", "application/json");
		request.body(request_params.toJSONString());

		// Response responce = httpRequest.request(Method.POST,"/assetadding");

		Response response = request.post("/assetadding");

		int statusCode = response.getStatusCode();
		System.out.println("The status code recieved: " + statusCode);

		try {

			Assert.assertEquals(statusCode, 200);

			log.info(" Successful response code received ");
			childTest.log(Status.INFO, " Successful response code received ");

			// softassert.assertEquals(statusCode, 200, "Status code mismatch");

			// System.out.println("Response body: " + response.getBody().asString());

//			System.out.println("==============================================");
//			System.out.println("Response body: " + response.jsonPath().prettify());

			// Fetch status of the response
			String res_status = response.jsonPath().getString("status");
			System.out.println(" Response status			: " + res_status);

			// Fetch status message
			String message = response.jsonPath().getString("message");
			System.out.println(" Response status message		: " + message);

			Map<Object, Object> asset_details = response.jsonPath().getMap("data");
			System.out.println(" Genarated Asset ID is 			: " + asset_details.get("id"));

			try {

				String flag = "true";

				Assert.assertEquals(message, "asset details added successfully");
				
				log.info(" response message as : " + message);
				childTest.log(Status.INFO, " response message as : " + message);
				log.info(" Asset location ID " + asset_details.get("locationId") + " created");

				if (asset_details.get("assetCode").equals(myMapData.get("assetCode").toString())) {
					log.info("Entered asset Code is matched with response");
					childTest.log(Status.INFO, "Entered asset Code is matched with response");
				} else {
					log.info("Mismatch : Entered asset Code and response asset Code");
					childTest.log(Status.INFO, "Mismatch : Entered asset Code and response asset Code");
					flag = "false";
				}

				if (asset_details.get("assetDescription").equals(myMapData.get("assetDescription").toString())) {
					log.info("Entered asset description is matched with response");
					childTest.log(Status.INFO, "Entered asset description is matched with response");
				} else {
					log.info("Mismatch : Entered asset description and response asset description");
					childTest.log(Status.INFO, "Mismatch : Entered asset description and response asset description");
					flag = "false";
				}

				int exp_locationId = Integer.parseInt(asset_details.get("locationId").toString());

				if (exp_locationId == locationId) {
					log.info("Entered location Id is matched with response");
					childTest.log(Status.INFO, "Entered location Id is matched with response");
				} else {
					log.info("Mismatch : Entered location Id and response location Id");
					childTest.log(Status.INFO, "Mismatch : Entered location Id and response location Id");
					flag = "false";
				}

				int exp_status = Integer.parseInt(asset_details.get("status").toString());

				//exp_status = 121;

				if (exp_status == status) {
					log.info("Entered status is matched with response");
					childTest.log(Status.INFO, "Entered status is matched with response");
				} else {
					log.info("Mismatch : Entered status and response status");
					childTest.log(Status.INFO, "Mismatch : Entered status and response status");
					flag = "false";
				}

				if (flag.equalsIgnoreCase("true")) {
					log.info(" Test case passed ");
					childTest.log(Status.INFO, " Test case passed ");
				} else {
					log.info(" Test case fail : one of the data is mismatch ");
					childTest.log(Status.INFO, " Test case fail : one of the data is mismatch ");
					// Assert.assertFalse(true, "one of the data is mismatch");
					softassert.fail("Test Fail : one of the data is mismatch");
				}

			} catch (AssertionError | Exception e) {
				log.info("Test fail: " + e);
				childTest.log(Status.INFO, "Test case fail : Response message mismatch ");
				// log.info("Test case fail : Response message mismatch");
				softassert.fail("Test case fail : Response message mismatch ");
				// softassert.assertEquals(message, "Duration of Trip details fetch successfully
				// success1", "Response message mismatch");
			}

		} catch (AssertionError e) {

			// softassert.assertEquals(statusCode, 200, "Status code mismatch");
			// log.info(" Test Fail : Successful response code not received");
			log.info(" Test Fail : " + e);
			childTest.log(Status.INFO, "Test Fail : Unsuccessful Response code received");
			softassert.fail("Test Fail : Unsuccessful Response code received");

		}

		softassert.assertAll();

	}

}
