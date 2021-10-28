package Asset_tracker_post;

import static io.restassured.RestAssured.given;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import utils.excel_utils;

public class get_005_get_assettrackingforui extends base{

	public static Logger log = LogManager.getLogger(get_005_get_assettrackingforui.class);
	static Response response;
	SoftAssert softassert = new SoftAssert();

	@Test(dataProvider = "g_assettrackingforui", dataProviderClass = excel_utils.class)
	public void assettrackingforui(Map myMapData) {
		
		parenTtest.assignCategory("Webservice : get");
		childTest = parenTtest.createNode("Verifying assettrackingforui webservice ");

		log.info(" ================================================= ");
		log.info(" Asset Tracking : assettrackingforui api test starts");

		String asset_Code = myMapData.get("asset_Code").toString();

		log.info(" Search with asset Code  " + asset_Code);
		childTest.log(Status.INFO, " Search with asset Code  " + asset_Code);

		response = given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).when()
				.get("http://assettracking.alpha-numero.com:8899/sccl/api/v1/assettrackingforui").then()
				.contentType(ContentType.JSON).extract().response();

		JsonPath jsonpath = response.jsonPath();

		// https://devqa.io/parse-json-response-rest-assured/

		// System.out.println("===============================================================");
		// System.out.println("Dumper response status\n");
		List<String> jsonResponse = jsonpath.getList("data.$");
		// System.out.println(" Total dumpers : " + jsonResponse.size());
		// System.out.println(jsonResponse);

		int statusCode = response.getStatusCode();
		System.out.println(" Response code				: " + response.getStatusCode());

		try {

			Assert.assertEquals(statusCode, 200);

			log.info(" Successful response code received ");
			childTest.log(Status.INFO, " Successful response code received ");

			// Fetch status of the response
			String status = jsonpath.getString("status");
			System.out.println(" Response status			: " + status);

			// Fetch status message
			String message = jsonpath.getString("message");
			System.out.println(" Response status message		: " + message);

			// softassert.assertEquals(response.getStatusCode(), 200, "Response Status code
			// mismatch");

			List<Map<Object, Object>> asset_tracking_details = jsonpath.getList("data");

			int count = asset_tracking_details.size();
//			
//			String s = dumperDataID.get(2).get("deviceMappingID").toString();
//			System.out.println(s);

			String index = null;
			String flag = "true";

			for (int i = 0; i < count; i++) {

				String fetch_asset_Code = asset_tracking_details.get(i).get("asset_Code").toString();

				if (fetch_asset_Code.equalsIgnoreCase(asset_Code) ) {

					log.info(" asset Code matched successfully");
					childTest.log(Status.INFO, " asset Code matched successfully");

					index = "data[" + i + "]";

					Map<String, String> asset_tracking_Data = jsonpath.getMap(index);

					if (asset_tracking_Data.get("room_Id").equals(myMapData.get("room_Id").toString())) {
						log.info(" room Id is matched with response");
						childTest.log(Status.INFO, " room Id is matched with response");
					} else {
						log.info(" Mismatch : room Id and response room Id");
						childTest.log(Status.INFO, " Mismatch : room Id and response room Id");
						flag = "false";
					}

					if (asset_tracking_Data.get("asset_Description").equals(myMapData.get("asset_Description").toString())) {
						log.info(" asset Description is matched with response");
						childTest.log(Status.INFO, " asset Description is matched with response");
					} else {
						log.info(" Mismatch : asset Description and response asset Description");
						childTest.log(Status.INFO, " Mismatch : asset Description and response asset Description");
						flag = "false";
					}
					
					if (asset_tracking_Data.get("room").equals(myMapData.get("room").toString())) {
						log.info(" room is matched with response");
						childTest.log(Status.INFO, " room is matched with response");
					} else {
						log.info(" Mismatch : room and response room");
						childTest.log(Status.INFO, " Mismatch : room and response room");
						flag = "false";
					}

					break;
				}

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
