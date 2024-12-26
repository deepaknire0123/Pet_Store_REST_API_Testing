/**
 * 
 */
package api.endPoints;

/**
 * Create User POST https://petstore.swagger.io/v2/user
 * Get user GET https://petstore.swagger.io/v2/user/{username}
 * Update user PUT https://petstore.swagger.io/v2/user/{username}
 * Delete user DELETE https://petstore.swagger.io/v2/user/{username}
 */
public class Routes {
	
	public static String baseUrl = "https://petstore.swagger.io/v2";
	
	//User Module
	public static String post_url = baseUrl + "/user";
	public static String get_url = baseUrl + "/user/{username}";
	public static String put_url = baseUrl + "/user/{username}";
	public static String delete_url = baseUrl + "/user/{username}";
	
	
	//Pet Module
	
	//store module urls
	
}
