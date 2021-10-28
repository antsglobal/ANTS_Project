package Asset_tracker_post;

import static org.testng.Assert.assertEquals;

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

public class post_001_assetlocationsadding extends base{

	public static Logger log = LogManager.getLogger(post_001_assetlocationsadding.class);

	@Test(dataProvider = "assetlocationsadding", dataProviderClass = excel_utils.class)
	public void assetLocationsAdding(Map myMapData) {
		
		parenTtest.assignCategory("Webservice : post");
		childTest = parenTtest.createNode("Verifying assetlocationsadding webservice ");

		log.info(" ================================================= ");
		log.info(" Asset Tracking : assetlocationsadding api test starts");

		RestAssured.baseURI = "http://assettracking.alpha-numero.com:8899/sccl/api/v1";
		RequestSpecification request = RestAssured.given();

		JSONObject request_params = new JSONObject();

		SoftAssert softassert = new SoftAssert();

		int cH_Antenna = Integer.parseInt(myMapData.get("chAntenna").toString());
		int subch = Integer.parseInt(myMapData.get("subch").toString());

		request_params.put("chAntenna", cH_Antenna);
		request_params.put("locationDescription", myMapData.get("locationDescription").toString());
		request_params.put("rFIDReaderCode", myMapData.get("rFIDReaderCode").toString());
		request_params.put("subch", subch);

		request.header("Content-type", "application/json");
		request.body(request_params.toJSONString());

		// Response responce = httpRequest.request(Method.POST,"/assetadding");

		Response response = request.post("/assetlocationsadding");

		int statusCode = response.getStatusCode();
		System.out.println("The status code recieved: " + statusCode);

		try {

			Assert.assertEquals(statusCode, 200);

			log.info(" Successful response code received ");
			childTest.log(Status.INFO, " Successful response code received ");

			// System.out.println("Response body: " + response.getBody().asString());

//			System.out.println("==============================================");
//			System.out.println("Response body: " + response.jsonPath().prettify());

			// Fetch status of the response
			String status = response.jsonPath().getString("status");
			System.out.println(" Response status			: " + status);

			// Fetch status message
			String message = response.jsonPath().getString("message");
			System.out.println(" Response status message		: " + message);

			Map<Object, Object> asset_locationdetails = response.jsonPath().getMap("data");

			System.out.println(" Created Asset location ID 		: " + asset_locationdetails.get("locationId"));

			try {

				String flag = "true";

				Assert.assertEquals(message, "asset Location saved successfully");
				log.info(" response message as : " + message);
				childTest.log(Status.INFO, " response message as : " + message);
				log.info(" Asset location ID " + asset_locationdetails.get("locationId") + " created");

				// softassert.assertEquals(message, "asset Location saved successfully", "status
				// message mismatch");

				if (asset_locationdetails.get("locationDescription")
						.equals(myMapData.get("locationDescription").toString())) {
					log.info("Entered location description is matched with response");
					childTest.log(Status.INFO, "Entered location description is matched with response");
				} else {
					log.info("Mismatch : Entered location description and response description");
					childTest.log(Status.INFO, "Mismatch : Entered location description and response description");
					flag = "false";
				}

				if (asset_locationdetails.get("rFIDReaderCode").equals(myMapData.get("rFIDReaderCode").toString())) {
					log.info("Entered rFIDReaderCode is matched with response");
					childTest.log(Status.INFO, "Entered rFIDReaderCode is matched with response");
				} else {
					log.info("Mismatch : Entered rFIDReaderCode and response rFIDReaderCode");
					childTest.log(Status.INFO, "Mismatch : Entered rFIDReaderCode and response rFIDReaderCode");
					flag = "false";
				}

				int exp_chAntenna = Integer.parseInt(asset_locationdetails.get("chAntenna").toString());
								
				if (exp_chAntenna == cH_Antenna) {
					log.info("Entered chAntenna is matched with response");
					childTest.log(Status.INFO, "Entered chAntenna is matched with response");
				} else {
					log.info("Mismatch : Entered chAntenna and response chAntenna");
					childTest.log(Status.INFO, "Mismatch : Entered chAntenna and response chAntenna");
					flag = "false";
				}

				int exp_subch = Integer.parseInt(asset_locationdetails.get("subch").toString());

				 //exp_subch = 12;

				if (exp_subch == subch) {
					log.info("Entered subch is matched with response");
					childTest.log(Status.INFO, "Entered subch is matched with response");
				} else {
					log.info("Mismatch : Entered subch and response subch");
					childTest.log(Status.INFO, "Mismatch : Entered subch and response subch");
					flag = "false";
				}

				if (flag.equalsIgnoreCase("true")) {
					log.info(" Test case passed ");
					childTest.log(Status.INFO, " Test case passed ");
				} else {
					log.info(" Test case fail : one of the data is mismatch ");
					childTest.log(Status.INFO, " Test case fail : one of the data is mismatch ");
					//Assert.assertFalse(true, "one of the data is mismatch");
					softassert.fail( "Test Fail : one of the data is mismatch");
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
