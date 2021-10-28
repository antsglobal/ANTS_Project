package Asset_tracker_post;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.excel_utils;

public class check_post_005_dumperLiveLocation {

	public static Logger log = LogManager.getLogger(check_post_005_dumperLiveLocation.class);

	@Test(dataProvider = "p_dumperLiveLocation", dataProviderClass = excel_utils.class)
	public void dumperLiveLocation(Map myMapData) {

		log.info(" ================================================= ");
		log.info(" Asset Tracking : dumperLiveLocation api test starts");

		RestAssured.baseURI = "http://assettracking.alpha-numero.com:8899/sccl/api/v1";
		RequestSpecification request = RestAssured.given();

		JSONObject request_params = new JSONObject();

		SoftAssert softassert = new SoftAssert();

		int deviceId = Integer.parseInt(myMapData.get("deviceId").toString());
		int id = Integer.parseInt(myMapData.get("id").toString());
		
		//String deviceId = myMapData.get("deviceId").toString();

		log.info(" Search with deviceId   " + deviceId);

		request_params.put("dumperId", deviceId);

		request.header("Content-type", "application/json");
		request.body(request_params.toJSONString());

		// Response responce = httpRequest.request(Method.POST,"/assetadding");

		Response response = request.post("/dumperLiveLocation");
				
		int statusCode = response.getStatusCode();
		
		//System.out.println(response.jsonPath().prettify());
		
		System.out.println("aaa");

		System.out.println("The status code recieved: " + statusCode);

		try {

			Assert.assertEquals(statusCode, 200);

			log.info(" Successful response code received ");

			// System.out.println("Response body: " + response.getBody().asString());

			System.out.println("==============================================");
			System.out.println("Response body: " + response.jsonPath().prettify());

			// Fetch status of the response
			String status = response.jsonPath().getString("status");
			System.out.println(" Response status			: " + status);

			// Fetch status message
			String message = response.jsonPath().getString("message");
			System.out.println(" Response status message		: " + message);

			Map<Object, Object> dumperLiveLocation_data = response.jsonPath().getMap("data");

			try {

				String flag = "true";

				Assert.assertEquals(statusCode, 200);
				log.info(" Successful response code received ");

				int exp_deviceId = Integer.parseInt(dumperLiveLocation_data.get("locationDescription").toString());

				if (deviceId==0) {

					log.info(" Device Id matched successfully");
					
					int exp_Id = Integer.parseInt(dumperLiveLocation_data.get("id").toString());


					if (id == exp_Id) {
						log.info("Id matched successfully");
					} else {
						log.info("Test fail : Mismatch - Id");
						flag = "false";
					}

					if (dumperLiveLocation_data.get("time_stamp")
							.equals(myMapData.get("time_stamp").toString())) {
						log.info("time_stamp matched successfully");
					} else {
						log.info("Test fail : Mismatch - time_stamp");
						flag = "false";
					}
					
					if (dumperLiveLocation_data.get("latitude")
							.equals(myMapData.get("latitude").toString())) {
						log.info("latitude matched successfully");
					} else {
						log.info("Test fail : Mismatch - latitude");
						flag = "false";
					}
					
					if (dumperLiveLocation_data.get("longitude")
							.equals(myMapData.get("longitude").toString())) {
						log.info("longitude matched successfully");
					} else {
						log.info("Test fail : Mismatch - longitude");
						flag = "false";
					}

					if (dumperLiveLocation_data.get("status")
							.equals(myMapData.get("status").toString())) {
						log.info("status matched successfully");
					} else {
						log.info("Test fail : Mismatch - status");
						flag = "false";
					}
					

					if (flag.equalsIgnoreCase("true")) {
						log.info(" Test case passed ");
					} else {
						log.info(" Test case fail : one of the data is mismatch ");
						Assert.assertFalse(true, "one of the data is mismatch with response data");
					}

				} else {

					log.info(" Test fail : Mismatch - Device Id ");

				}

			} catch (Exception e) {
				log.info(e);
				System.out.println("Test case fail");
				// softassert.assertEquals(message, "asset Location saved successfully",
				// "response status message mismatch");
				Assert.assertFalse(true,
						"response status message mismatch - expected message : asset Location saved successfully : actual message : {0} "
								+ message);
			}

		} catch (Exception e) {
			log.info(e);
			log.info("Test case faild");
			softassert.assertEquals(statusCode, 200, "Status code mismatch");
		}

		softassert.assertAll();

	}

}
