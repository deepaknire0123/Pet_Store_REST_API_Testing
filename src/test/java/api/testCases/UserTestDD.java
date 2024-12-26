/**
 * 
 */
package api.testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import api.endPoints.UserEndPoints;
import api.payload.User;
import api.utilities.DataProviders;
import io.restassured.response.Response;

/**
 * Data Driven Testing 
 */
public class UserTestDD {
		
	@Test(priority = 1, dataProvider = "AllData", dataProviderClass = DataProviders.class)
	public void testCreateAndGetUser(String userId, String UserName, String fname, String lname, String email, String pwd, String phone) 
	{
		User userPayload = new User();
		
		userPayload.setId(Integer.parseInt(userId));
		userPayload.setUsername(UserName);
		userPayload.setFirstName(fname);
		userPayload.setLastName(lname);
		userPayload.setEmail(lname);
		userPayload.setPassword(email);
		userPayload.setPhone(phone);
		
	    // Create User
	    Response createResponse = UserEndPoints.createUserEndPoints(userPayload);
		System.out.println("User creation data");
	    createResponse.then().log().all();
	    
	    // Assert successful creation
	    Assert.assertEquals(createResponse.getStatusCode(), 200);
	    
	    // Extract username from payload or pass it directly
	    String username = userPayload.getUsername();
	    
	    // Fetch User Details
	    Response getResponse = UserEndPoints.getUser(username);
	    System.out.println("Read user validated data");
	    getResponse.then().log().all();
	    
	    // Validate user details
	    Assert.assertEquals(getResponse.getStatusCode(), 200);
	    Assert.assertEquals(getResponse.jsonPath().getString("username"), username);
	}
	
	@Test(priority=2,dataProvider = "UserNamesData", dataProviderClass = DataProviders.class)
	public void testGetUserData(String username)
	{
		Response response = UserEndPoints.getUser(username);
		System.out.println("Get User Data.");
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(),200);
	}
	
	@Test(priority=3,dataProvider = "UserNamesData", dataProviderClass = DataProviders.class)
	public void testDeleteUser(String username)
	{

		Response response = UserEndPoints.deleteUser(username);

		System.out.println("Delete User Data.");

		//log response
		response.then().log().all();

		//validation
		Assert.assertEquals(response.getStatusCode(),200);



	}

}
