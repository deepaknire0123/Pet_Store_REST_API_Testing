/**
 * 
 */
package api.endPoints;
import static io.restassured.RestAssured.*;

import api.payload.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
/**
 * Creating a Request
 */
public class UserEndPoints {
	
	public static Response createUserEndPoints(User payload)
	{
		Response response = given()
		.accept(ContentType.JSON)
		.contentType(ContentType.JSON)
		.body(payload)
		
		.when()
		.post(Routes.post_url);
		
		return response;
	}
	
	public static Response getUser(String UserName)
	{
		Response response = given()
				.accept(ContentType.JSON)
				.pathParam("username", UserName)
				
				.when()
				.get(Routes.get_url);
		
		return response;						
	}
	
	public static Response updateUser(String userName, User payload)
	{
		Response response = given()
				.accept(ContentType.JSON)
				.contentType(ContentType.JSON)
				.pathParam("username", userName)
				.body(payload)
				
				.when()
				.put(Routes.put_url);
		
		return response;
	}
	
	public static Response deleteUser(String userName)
	{
		Response response =given()
				.accept(ContentType.JSON)
				.pathParam("username", userName)
				
				.when()
				.delete(Routes.delete_url);
		
		return response;
	}
	
	/**
	 * A PATCH request is typically used to update a subset of a resource.
	 public static Response patchUser(String userName, Map<String, String> partialUpdatePayload) 
	 {
	    Response response = given()
	            .accept(ContentType.JSON)
	            .contentType(ContentType.JSON)
	            .pathParam("username", userName)
	            .body(partialUpdatePayload) // Partial update as a JSON payload
	            .when()
	            .patch(Routes.patch_url); // Assuming Routes.patch_url is defined
	    
	    return response;
	}
	
	//A HEAD request checks if a resource exists without returning the body.
	public static Response headUser(String userName) 
	{
	    Response response = given()
	            .accept(ContentType.JSON)
	            .pathParam("username", userName)
	            .when()
	            .head(Routes.get_url); // Using the same URL as GET
	
	    return response;
	}

	 */

}
