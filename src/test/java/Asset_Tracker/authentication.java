package Asset_Tracker;

import org.testng.annotations.Test;

import io.restassured.RestAssured;

import static io.restassured.RestAssured.*;

public class authentication {
	
	//https://www.youtube.com/watch?v=w-1EvCAszgE
	
	//  token oauth
	// https://www.youtube.com/watch?v=lyQmRjBwMeg&list=PL6flErFppaj08LFolWioWyjfSGtCWcvVp&index=4
	
	@Test
	public void authentication_check() {
		
		int statuscode = RestAssured.given()
								.auth().preemptive()
								.basic("ToolsQA","TestPassword")
								.when()
								.get("https://restapi.demoqa.com/authentication/CheckForAuthentication")
								.getStatusCode();
								
								
		System.out.println("Status coe : " + statuscode);
		
	}

}
