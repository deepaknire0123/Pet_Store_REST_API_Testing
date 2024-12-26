/**
 * 
 */
package api.endPoints;
import static io.restassured.RestAssured.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import api.payload.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
/**
 * Creating a Request
 */
public class UserEndPoints2 {
	
	// Load properties file using Properties class
    private static Properties properties;
    
    static {
        properties = new Properties();
        try (FileInputStream fileInput = new FileInputStream("C:\\workspace\\PetStore_RestAssuredFramework\\src\\test\\resources\\routes.properties")) 
        {
            properties.load(fileInput);  // Load the properties file
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
	
	public static Response createUserEndPoints(User payload)
	{
		String postUrl = properties.getProperty("post_url");
		
		Response response = given()
		.accept(ContentType.JSON)
		.contentType(ContentType.JSON)
		.body(payload)
		
		.when()
		.post(postUrl);
		
		return response;
	}
	
	public static Response getUser(String UserName)
	{
		Response response = given()
				.accept(ContentType.JSON)
				.pathParam("username", UserName)
				
				.when()
				.get(properties.getProperty("get_url"));
		
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
				.put(properties.getProperty("put_url"));
		
		return response;
	}
	
	public static Response deleteUser(String userName)
	{
		Response response =given()
				.accept(ContentType.JSON)
				.pathParam("username", userName)
				
				.when()
				.delete(properties.getProperty("delete_url"));
		
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
