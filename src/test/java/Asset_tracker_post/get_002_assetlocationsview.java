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

public class get_002_assetlocationsview extends base{

	public static Logger log = LogManager.getLogger(get_002_assetlocationsview.class);
	static Response response;
	SoftAssert softassert = new SoftAssert();

	@Test(dataProvider = "g_assetlocationsview", dataProviderClass = excel_utils.class)
	public void assetLocationView(Map myMapData) {
		
		parenTtest.assignCategory("Webservice : get");
		childTest = parenTtest.createNode("Verifying assetlocationsview webservice ");
		
		log.info(" ================================================= ");
		log.info(" Asset Tracking : assetlocationsview api test starts");

		int locationId = Integer.parseInt(myMapData.get("locationId").toString());
		int chAntenna = Integer.parseInt(myMapData.get("chAntenna").toString());
		int subch = Integer.parseInt(myMapData.get("subch").toString());

		response = given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).when()
				.get("http://assettracking.alpha-numero.com:8899/sccl/api/v1/assetlocationsview").then()
				.contentType(ContentType.JSON).extract().response();

		JsonPath jsonpath = response.jsonPath();

		// https://devqa.io/parse-json-response-rest-assured/

//		System.out.println("===============================================================");
//		System.out.println("Asset Location response status\n");
		List<String> jsonResponse = jsonpath.getList("data.$");
//		System.out.println(" Total Asset 				: " + jsonResponse.size());
		// System.out.println(jsonResponse);

		int statusCode = response.getStatusCode();
		System.out.println(" Response code				: " + statusCode);

		// Fetch status of the response
		String status = jsonpath.getString("status");
		System.out.println(" Response status			: " + status);

		// Fetch status message
		String message = jsonpath.getString("message");
		System.out.println(" Response status message		: " + message);

		try {

			Assert.assertEquals(statusCode, 200);

			log.info(" Successful response code received ");
			childTest.log(Status.INFO, " Successful response code received ");

			List<Map<Object, Object>> location_data = jsonpath.getList("data");

			int count = location_data.size();

			String index = null;
			String flag = "true";

			for (int i = 0; i < count; i++) {

				int fetch_locationId = Integer.parseInt(location_data.get(i).get("locationId").toString());

				if (fetch_locationId == locationId) {

					log.info(" location ID matched successfully");
					childTest.log(Status.INFO, " location ID matched successfully");

					index = "data[" + i + "]";

					Map<Object, Object> locationID_Data = jsonpath.getMap(index);

					if (locationID_Data.get("locationDescription")
							.equals(myMapData.get("locationDescription").toString())) {
						log.info(" location Description is matched with response");
						childTest.log(Status.INFO, " location Description is matched with response");
					} else {
						log.info(" Mismatch : location Description and response location Description");
						childTest.log(Status.INFO, " Mismatch : location Description and response location Description");
						flag = "false";
					}

					if (locationID_Data.get("rFIDReaderCode").equals(myMapData.get("rFIDReaderCode").toString())) {
						log.info(" rFIDReaderCode is matched with response");
						childTest.log(Status.INFO, " rFIDReaderCode is matched with response");
					} else {
						log.info(" Mismatch : rFIDReaderCode and response rFIDReaderCode");
						childTest.log(Status.INFO, " Mismatch : rFIDReaderCode and response rFIDReaderCode");
						flag = "false";
					}

					int exp_chAntenna = Integer.parseInt(locationID_Data.get("chAntenna").toString());

					if (exp_chAntenna == chAntenna) {
						log.info(" chAntenna is matched with response");
						childTest.log(Status.INFO, " chAntenna is matched with response");
					} else {
						log.info("Mismatch : chAntenna and response chAntenna");
						childTest.log(Status.INFO, "Mismatch : chAntenna and response chAntenna");
						flag = "false";
					}

					int exp_subch = Integer.parseInt(locationID_Data.get("subch").toString());

					if (exp_subch == subch) {
						log.info(" subch is matched with response");
						childTest.log(Status.INFO, " subch is matched with response");
					} else {
						log.info("Mismatch : subch and response subch");
						childTest.log(Status.INFO, "Mismatch : subch and response subch");
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
//				Assert.assertFalse(true, "one of the data is mismatch");
				softassert.fail("Test Fail : one of the data is mismatch");
			}
			// softassert.assertEquals(response.getStatusCode(), 200, "Response Status code
			// mismatch");

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
