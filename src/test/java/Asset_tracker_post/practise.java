package Asset_tracker_post;

import static io.restassured.RestAssured.given;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.excel_utils;

public class practise {
	

	public static Logger log = LogManager.getLogger(practise.class);
	
	@Test()
	void demo(){
		
		try {
			
						
			log.info("test starts");
			Assert.assertTrue(false);
			
		}catch(AssertionError | Exception e) {
			log.info(e);
			log.info("Test case fail");
			Assert.fail("test fail");
			
		}
	}
}
