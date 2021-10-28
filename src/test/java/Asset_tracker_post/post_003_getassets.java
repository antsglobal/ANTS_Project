package Asset_tracker_post;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

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
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.excel_utils;

public class post_003_getassets extends base{
	
	public static Logger log = LogManager.getLogger(post_003_getassets.class);

	@Test(dataProvider = "getassets", dataProviderClass = excel_utils.class)
	public void getAssets(Map myMapData) {
		
		parenTtest.assignCategory("Webservice : post");
		childTest = parenTtest.createNode("Verifying getassets webservice ");

		log.info(" ================================================= ");
		log.info(" Asset Tracking : get assets api test starts");

		String asset_code_value = myMapData.get("asset_code").toString();
		String location_id_value = myMapData.get("location_id").toString();

		RestAssured.baseURI = "http://assettracking.alpha-numero.com:8899/sccl/api/v1";
		RequestSpecification request = RestAssured.given();

		JSONObject request_params = new JSONObject();

		SoftAssert softassert = new SoftAssert();

		request_params.put("locationId", location_id_value);

		request.header("Content-type", "application/json");
		request.body(request_params.toJSONString());

		// Response responce = httpRequest.request(Method.POST,"/assetadding");

		Response response = request.post("/getassets");
		childTest.log(Status.INFO, "Search with location ID " + location_id_value);
		log.info("Search with location ID " + location_id_value);

		JsonPath jsonpath = response.jsonPath();

		int statusCode = response.getStatusCode();
		System.out.println("The status code recieved: " + statusCode);

		try {

			Assert.assertEquals(statusCode, 200);
			
			log.info(" Successful response code received ");
			childTest.log(Status.INFO, "Successful response code received ");

			//softassert.assertEquals(statusCode, 200, "Status code mismatch");

			// System.out.println("Response body: " + response.getBody().asString());

			//System.out.println("==============================================");
			//System.out.println("Response body: " + jsonpath.prettify());

			List<String> assets = jsonpath.getList("$");
			int asset_count = assets.size();
			int flag = 0;

			for (int i = 0; i < asset_count; i++) {

				String asset_code_index = "assetCode[" + i + "]";
				String asset_code = jsonpath.getString(asset_code_index);

				if (asset_code.equalsIgnoreCase(asset_code_value)) {
					
					flag = 1;
					
					log.info(" Asset code matched successfully ");
					childTest.log(Status.INFO," Asset code matched successfully ");

					String asset_code__id_index = "id[" + i + "]";
					String asset_code__desc_index = "assetDescription[" + i + "]";
					String asset_code__location_id_index = "locationId[" + i + "]";
					String asset_code__status_index = "status[" + i + "]";

					String id = jsonpath.getString(asset_code__id_index);
					String asset_codedesc = jsonpath.getString(asset_code__desc_index);
					String location_id = jsonpath.getString(asset_code__location_id_index);
					String status = jsonpath.getString(asset_code__status_index);

					
					try {
						
						Assert.assertEquals(location_id, location_id_value);
						
						log.info(" Location ID matched successfully ");
						childTest.log(Status.INFO, " Location ID matched successfully");
						log.info(" Test case passed ");
						childTest.log(Status.INFO, " Test case passed");
						
//						System.out.println("==============================================");
//						System.out.println("Verifying the following asset code  ");
//						System.out.println(asset_code);
//						System.out.println(id);
//						System.out.println(asset_codedesc);
//						System.out.println(location_id);
//						System.out.println(status);
						
					}catch(AssertionError e) {
						
						log.info("Test Fail : Location ID mismatch");
						childTest.log(Status.INFO, "Test Fail : Location ID mismatch ");
						//softassert.assertEquals(location_id, location_id_value, " Location ID mesmatch");
//						Assert.assertTrue(false, "Test Fail : Location ID mismatch  ");
						softassert.fail("Test case fail : Location ID mismatch  ");
					}
					
					break;
				}
			}
			
			if(flag == 0) {
				
				childTest.log(Status.INFO, "Test Fail : Asset code not found for validate the data : " + asset_code_value);
				log.info("Test Fail : Asset code " + asset_code_value + " not found for entered location ID  " );
				childTest.log(Status.INFO, "Test Fail : Asset code " + asset_code_value + " not found for entered location ID  ");
//				Assert.assertTrue(false, "Test Fail : Asset code not found for validate the data ");
				softassert.fail( "Test Fail : Asset code not found for entered location ID");
				
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
