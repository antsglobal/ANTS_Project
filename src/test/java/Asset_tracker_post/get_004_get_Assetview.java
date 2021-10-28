package Asset_tracker_post;

import static io.restassured.RestAssured.given;

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
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.excel_utils;

public class get_004_get_Assetview extends base{

	public static Logger log = LogManager.getLogger(get_004_get_Assetview.class);
	static Response response;
	SoftAssert softassert = new SoftAssert();

	@Test(dataProvider = "g_assetview", dataProviderClass = excel_utils.class)
	public void assetview(Map myMapData) {
		
		parenTtest.assignCategory("Webservice : get");
		childTest = parenTtest.createNode("Verifying assetview webservice ");

		log.info(" ================================================= ");
		log.info(" Asset Tracking : assetview api test starts");

		int asset_ID = Integer.parseInt(myMapData.get("id").toString());
		int location_ID = Integer.parseInt(myMapData.get("locationId").toString());
		int loc_status = Integer.parseInt(myMapData.get("status").toString());

		log.info(" Search with asset code " + myMapData.get("assetCode").toString());
		childTest.log(Status.INFO, " Search with asset code " + myMapData.get("assetCode").toString());

		response = given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).when()
				.get("http://assettracking.alpha-numero.com:8899/sccl/api/v1/assetview").then()
				.contentType(ContentType.JSON).extract().response();

		JsonPath jsonpath = response.jsonPath();

		int statusCode = response.getStatusCode();
		System.out.println("assetview Response code: " + statusCode);

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

			// Map<Object, Object> asset_view = response.jsonPath().getMap("data");
			// List<Map<Object, Object>> asset_view = jsonpath.getList("data");

			try {

				String flag = "true";

				Assert.assertEquals(message, "asset details added successfully");
				log.info(" response message as : " + message);
				childTest.log(Status.INFO, " response message as : " + message);

				List<Map<Object, Object>> asset_view = jsonpath.getList("data");

				int count = asset_view.size();

				String index = null;
				// String flag = "true";

				for (int i = 0; i < count; i++) {

					String fetch_assetCode = asset_view.get(i).get("assetCode").toString();

					if (fetch_assetCode.equalsIgnoreCase(myMapData.get("assetCode").toString())) {

						log.info(" Asset code matched successfully");
						childTest.log(Status.INFO, " Asset code matched successfully");

						index = "data[" + i + "]";

						Map<Object, Object> assetCode_Data = jsonpath.getMap(index);

						int exp_asset_Id = Integer.parseInt(assetCode_Data.get("id").toString());

						if (exp_asset_Id == asset_ID) {
							log.info("asset id is matched with response");
							childTest.log(Status.INFO, "asset id is matched with response");
						} else {
							log.info("Mismatch : asset ID and response asset ID");
							childTest.log(Status.INFO, "Mismatch : asset ID and response asset ID");
							flag = "false";
						}

						if (assetCode_Data.get("assetDescription")
								.equals(myMapData.get("assetDescription").toString())) {
							log.info("asset description is matched with response");
							childTest.log(Status.INFO, "asset description is matched with response");
						} else {
							log.info("Mismatch : asset description and response asset description");
							childTest.log(Status.INFO, "Mismatch : asset description and response asset description");
							flag = "false";
						}

						int exp_locationId = Integer.parseInt(assetCode_Data.get("locationId").toString());

						if (exp_locationId == location_ID) {
							log.info("location Id is matched with response");
							childTest.log(Status.INFO, "location Id is matched with response");
						} else {
							log.info("Mismatch : location Id and response location Id");
							childTest.log(Status.INFO, "Mismatch : location Id and response location Id");
							flag = "false";
						}

						int exp_status = Integer.parseInt(assetCode_Data.get("status").toString());

						// exp_status = 12;

						if (exp_status == loc_status) {
							log.info("status is matched with response");
							childTest.log(Status.INFO, "status is matched with response");
						} else {
							log.info("Mismatch : status and response status");
							childTest.log(Status.INFO, "Mismatch : status and response status");
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

						break;
					}
				}

			} catch(AssertionError | Exception e) {
				log.info("Test fail: " + e);
				childTest.log(Status.INFO, "Test case fail : Response message mismatch ");
				//log.info("Test case fail : Response message mismatch");
				softassert.fail("Test case fail : Response message mismatch ");
				//softassert.assertEquals(message, "Duration of Trip details fetch successfully success1", "Response message mismatch");
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
