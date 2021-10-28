package Asset_tracker_post;

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

import static io.restassured.RestAssured.*;

import java.util.List;
import java.util.Map;

public class get_001_Dumper_details extends base{

	public static Logger log = LogManager.getLogger(get_001_Dumper_details.class);
	static Response response;
	SoftAssert softassert = new SoftAssert();

	@Test(dataProvider = "g_alldumperids", dataProviderClass = excel_utils.class)
	public void allDumperIds(Map myMapData) {
		
		parenTtest.assignCategory("Webservice : get");
		childTest = parenTtest.createNode("Verifying alldumperids webservice ");
		

		log.info(" ================================================= ");
		log.info(" Asset Tracking : alldumperids api test starts");
		

		int deviceId = Integer.parseInt(myMapData.get("deviceId").toString());
		
		log.info(" Search with device ID " + deviceId);
		childTest.log(Status.INFO, " Search with device ID " + deviceId);

		response = given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).when()
				.get("http://assettracking.alpha-numero.com:8899/sccl/api/v1/alldumperids").then()
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

			log.info(	" Successful response code received ");
			childTest.log(Status.INFO," Successful response code received " );

			// Fetch status of the response
			String status = jsonpath.getString("status");
			System.out.println(" Response status			: " + status);

			// Fetch status message
			String message = jsonpath.getString("message");
			System.out.println(" Response status message		: " + message);

			// softassert.assertEquals(response.getStatusCode(), 200, "Response Status code
			// mismatch");

			List<Map<String, String>> dumperDataID = jsonpath.getList("data");

			int count = dumperDataID.size();
//			
//			String s = dumperDataID.get(2).get("deviceMappingID").toString();
//			System.out.println(s);


			String index = null;
			String flag = "true";

			for (int i = 0; i < count; i++) {

				int fetch_dumperid = Integer.parseInt(dumperDataID.get(i).get("deviceId").toString());

				if (fetch_dumperid == deviceId) {
					
					log.info(" Device ID matched successfully");
					childTest.log(Status.INFO, " Device ID matched successfully");

					index = "data[" + i + "]";

					Map<String, String> dumper_Data = jsonpath.getMap(index);

					if (dumper_Data.get("deviceCategory").equals(myMapData.get("deviceCategory").toString())) {
						log.info(" Device Category is matched with response");
						childTest.log(Status.INFO, " Device Category is matched with response");
					} else {
						log.info(" Mismatch : Device Category and response device Category");
						childTest.log(Status.INFO, " Mismatch : Device Category and response device Category");
						flag = "false";
					}
					
					if (dumper_Data.get("deviceMappingID").equals(myMapData.get("deviceMappingID").toString())) {
						log.info(" Device MappingID is matched with response");
						childTest.log(Status.INFO, " Device MappingID is matched with response");
					} else {
						log.info(" Mismatch : Device MappingID and response device MappingID");
						childTest.log(Status.INFO, " Mismatch : Device MappingID and response device MappingID");
						flag = "false";
					}

					break;
				}

			}
			
			if( flag.equalsIgnoreCase("true") ) {
				log.info(" Test case passed ");
				childTest.log(Status.INFO, " Test case passed ");
			}else {
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
