/**
 * 
 */
package api.testCases;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;
import static org.hamcrest.Matchers.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import api.endPoints.UserEndPoints;
import api.endPoints.UserEndPoints2;
import api.payload.User;
import io.restassured.response.Response;

/**
 * 
 */
public class UserTest {
	
	Faker faker;
	User userPayload;
	public static Logger logger;
	
	@BeforeClass
	public void generateTestData()
	{
		faker = new Faker();
		userPayload = new User();
		
		userPayload.setId(Math.abs(faker.idNumber().hashCode())); //hashCode generates unique id
		//userPayload.setId(faker.number().numberBetween(1, Integer.MAX_VALUE)); //only for +ve number
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		logger = LogManager.getLogger("PetStore_RestAssuredFramework");
	}
	
	@AfterClass
	public void cleanUp() 
	{
	    UserEndPoints2.deleteUser(userPayload.getUsername());
	}
	
	@Test(priority = 1)
	public void testCreateAndGetUser() {
	    // Create User
	    Response createResponse = UserEndPoints2.createUserEndPoints(userPayload);
		System.out.println("User creation data");
	    createResponse.then().log().all();
	    
	    // Assert successful creation
	    Assert.assertEquals(createResponse.getStatusCode(), 200);
	    
	    // Extract username from payload or pass it directly
	    String username = userPayload.getUsername();
	    
	    logger.info("Create User executed");
	    
	    // Fetch User Details
	    Response getResponse = UserEndPoints2.getUser(username);
	    System.out.println("Read user validated data");
	    getResponse.then().log().all();
	    
	    logger.info("Reading the created user data");
	    // Validate user details
	    Assert.assertEquals(getResponse.getStatusCode(), 200);
	    Assert.assertEquals(getResponse.jsonPath().getString("username"), username);
	}
	
//	@Test(priority = 1)
//	public void testCreateUser()
//	{
//		Response response = UserEndPoints.createUserEndPoints(userPayload);
//		
//		//log the response
//		response.then().log().all();
//		
//		//validation
//		Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 but found: " + response.getStatusCode() + "Unexpected status code");
//		
////		// Extract user ID from the response
////	    String userId = response.jsonPath().getString("message");
////	    
////	    // Assert that user ID is not null or empty
////	    Assert.assertNotNull(userId, "User ID in the response is null");
////	    
////	    // Send GET request to validate user details
////	    Response getUserResponse = UserEndPoints.getUser(userId); // problem is we pass username as parameter not msg
////	    getUserResponse.then().log().all();
////	    
////	    // Assert GET response status code
////	    Assert.assertEquals(getUserResponse.getStatusCode(), 200, "Expected 200 but found: " + getUserResponse.getStatusCode());
////	    
////	    // Validate the user details
////	    Assert.assertEquals(getUserResponse.jsonPath().getString("username"), userPayload.getUsername(), "Username does not match");
////	    Assert.assertEquals(getUserResponse.jsonPath().getString("email"), userPayload.getEmail(), "Email does not match");
////	    Assert.assertEquals(getUserResponse.jsonPath().getString("firstName"), userPayload.getFirstName(), "First name does not match");
//
//	}
	
	@Test(priority = 2, dependsOnMethods = "testCreateAndGetUser")
	public void testGetUserData()
	{
		Response response = UserEndPoints2.getUser(userPayload.getUsername());
		
		//log the response
		System.out.println("Read user data");
		response.then().log().all();
		
		//validation
		response.then().time(lessThan(2000L)); // Ensure response time is less than 2 seconds for dynamic testing
		Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 but found: " + response.getStatusCode() + "Unexpected status code");
		Assert.assertEquals(response.jsonPath().getString("username"), userPayload.getUsername(), "Username mismatch");
		
		logger.info("Get User data executed");
	}
	
	@Test(priority = 3, dependsOnMethods = "testCreateAndGetUser")
	public void testUpdateUser()
	{
		userPayload.setFirstName(faker.name().firstName());
		Response updateResponse = UserEndPoints2.updateUser(userPayload.getUsername(), userPayload);
		
		//log response	
		System.out.println("update user data");
		updateResponse.then().log().all(); 
		//response.then().log().status().log().body(); - log specific parts of the response (e.g., headers, body) for clarity 
		
		//validation
		Assert.assertEquals(updateResponse.getStatusCode(), 200, "Expected 200 but found: " + updateResponse.getStatusCode() + "Unexpected status code");
		
		//verify by sending request
		Response responsePostUpdate = UserEndPoints2.getUser(userPayload.getUsername());
		
		//log the get response 
		System.out.println("Read user data after update ");
		responsePostUpdate.then().log().all();
		
		//validate
		Assert.assertEquals(responsePostUpdate.getStatusCode(), 200);
		Assert.assertEquals(responsePostUpdate.jsonPath().getString("username"), userPayload.getUsername());
		
		logger.info("Update user executed");
	}
	
	@Test(priority = 4, dependsOnMethods = "testCreateAndGetUser")
	public void testDeleteUser()
	{
		Response response = UserEndPoints2.deleteUser(userPayload.getUsername());
		
		//log response
		System.out.println("delete user data");
		response.then().log().all();
		
		//validation
		Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 but found: " + response.getStatusCode() + "Unexpected status code");	
		
		//check that attempting to retrieve the user afterward returns a 404 or an appropriate error
		Response getResponse = UserEndPoints2.getUser(userPayload.getUsername());
		Assert.assertEquals(getResponse.getStatusCode(), 404, "User still exists after deletion");

		logger.info("Delete user executed");
	}
	
	
	
	
	
	/**
	 @Test(priority = 5)
public void testPatchUser() 
{
    // Prepare partial update payload
    Map<String, String> partialUpdatePayload = new HashMap<>();
    partialUpdatePayload.put("firstName", faker.name().firstName());

    Response response = UserEndPoints.patchUser(userPayload.getUsername(), partialUpdatePayload);

    // Log and validate response
    response.then().log().all();
    Assert.assertEquals(response.getStatusCode(), 200, "Patch user status code does not match");

    // Validate the updated field
    Response getResponse = UserEndPoints.getUser(userPayload.getUsername());
    Assert.assertEquals(getResponse.jsonPath().getString("firstName"), partialUpdatePayload.get("firstName"),
                        "First name was not updated");
}


@Test(priority = 6)
public void testHeadUser() {
    Response response = UserEndPoints.headUser(userPayload.getUsername());

    // Log response status
    response.then().log().status();
    
    // Validate response
    Assert.assertEquals(response.getStatusCode(), 200, "HEAD user status code does not match");
    Assert.assertNull(response.body().asString(), "HEAD request returned a body");
}


	 */
	
	

}



