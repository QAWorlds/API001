package API_TESTCASES;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;

public class HTTP_Request_001 {
int id;





    @Test(priority = 1)
    public void GetUser() {                                     // ✅ Get Data
        given() .baseUri("https://reqres.in/api") 
        .when().get("/users?page=2")
        .then().statusCode(200)
        .body("page", equalTo(2))
        .log().all();
    }

    
    
    
    
    @Test(priority = 2)
    public void PostUser() {                                    // ✅ Create Data
        HashMap data = new HashMap();
        data.put("name", "aravind");
        data.put("job", "tester");

     id=  given()
        .contentType("application/json")
        .body(data)
        .when().post("https://reqres.in/api/users") 
        .jsonPath().getInt("id");
        
        
   
      /*  .then().statusCode(201)
        .log().all();*/
    }
    
    
    @Test(priority = 3, dependsOnMethods = {"PostUser"})

    public void PutUser() {                                       // ✅ Update Data
        HashMap data = new HashMap();
        data.put("name", "aravind");
        data.put("job", "Automation");

       given()
        .contentType("application/json")
        .body(data)
        .when().put("https://reqres.in/api/users/"+id) 
        
        
        
        .then().statusCode(200)
        .log().all();
    }
    
    
    
    @Test(priority = 4)
    public void DeleteUser() {                                    // ✅ Delete Data
    	 given()
       
         .when().delete("https://reqres.in/api/users/"+id) 
        
         
         
         .then().statusCode(204)
         .log().all();
     }
     }
