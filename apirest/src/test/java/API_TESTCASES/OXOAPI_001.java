package API_TESTCASES;

import org.testng.annotations.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.response.Response;

public class OXOAPI_001 {
   
   String token;
   int increment_id;
   int item_id;
   int total_qty_ordered;
   

   int ship;




   String orderId = "12830223";

    @Test(priority = 1)
    public void APIKey() {
        String payload = "{ \"username\": \"qa-api\", \"password\": \"R2K261GbPSaI\" }";

        token =
            given()
                .contentType("application/json")
                .body(payload)
            .when()
                .post("https://na-preprod.hele.digital/rest/V1/integration/admin/token")
            .then()
                .statusCode(200)
                .log().all()
                .extract().asString() // Token is returned as plain string
        .replace("\"", "");
        System.out.println("Bearer Token: " + token);
    }
    
    
    

    @Test(priority = 2, dependsOnMethods = "APIKey")
    public void GetOrderDetails() {
      

        Response response = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + token)
        .when()
            .get("https://na-preprod.hele.digital/rest/all/V1/orders/" + orderId)
        .then()
            .statusCode(200)
            .log().body()
            .extract().response();

        // ✅ Extracting the data
        String increment_id = response.path("increment_id");
        int total_qty_ordered = response.path("total_qty_ordered");

        // For item_id, assuming it exists
        int item_id = response.path("items[0].item_id");

        // ✅ Print values to verify
        System.out.println("Increment ID: " + increment_id);
        System.out.println("Item ID: " + item_id);
        System.out.println("Total Qty Ordered: " + total_qty_ordered);
    }


   // @Test(priority = 3, dependsOnMethods = "GetOrderDetails")
    public void Ship() {
        String payload = "{\n" +
            "  \"notify\": false,\n" +
            "  \"items\": [\n" +
            "    {\n" +
            "      \"order_item_id\": " + item_id + ",\n" +
            "      \"qty\": 2.0\n" +
            "    }\n" +
            "  ],\n" +
            "  \"tracks\": [\n" +
            "    {\n" +
            "      \"track_number\": \"6221192641458\",\n" +
            "      \"title\": \"FedEx\",\n" +
            "      \"carrier_code\": \"fedex\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"arguments\": {\n" +
            "    \"extension_attributes\": {\n" +
            "      \"delivery_number\": \"2983558\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

        Response response = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + token)
            .body(payload)
        .when()
            .post("https://na-preprod.hele.digital/rest/all/V1/order/+orderId+/ship") // Use entity_id, not increment_id
        .then()
            .log().body()
            .extract().response();

        int statusCode = response.statusCode();
      //  Assert.assertEquals(statusCode, 200, "Expected status 200 but got: " + statusCode);
    }
    
    
    


    @Test(priority = 3)
    public void invoice() { 
    	
    	 String payload = "{\n"
    	 		+ "			\"items\": [\n"
    	 		+ "				{\n"
    	 		+ "					\"order_item_id\": + item_id +,\n"
    	 		+ "					\"qty\": 2\n"
    	 		+ "                }\n"
    	 		+ "               \n"
    	 		+ "			],\n"
    	 		+ "			\"notify\": false,\n"
    	 		+ "			\"appendComment\": false,\n"
    	 		+ "			\"capture\": true,\n"
    	 		+ "			\"arguments\": {\n"
    	 		+ "				\"extension_attributes\": {\n"
    	 		+ "					\"delivery_number\": \"2983532\",\n"
    	 		+ "					\"oracle_customer_number\": \"\"\n"
    	 		+ "				}\n"
    	 		+ "			}\n"
    	 		+ "		}\n"
    	 		+ " ";

    	        Response response = given()
    	            .contentType("application/json")
    	            .header("Authorization", "Bearer " + token)
    	            .body(payload)
    	        .when()
    	            .post("https://na-preprod.hele.digital/rest/hydroflask/V1/order/+orderId+/invoice") // Use entity_id, not increment_id
    	        .then()
    	            .log().body()
    	            .extract().response();

    	        int statusCode = response.statusCode();
    	      //  Assert.assertEquals(statusCode, 200, "Expected status 200 but got: " + statusCode);
    	    }
    	    
    	
    	
      
}
