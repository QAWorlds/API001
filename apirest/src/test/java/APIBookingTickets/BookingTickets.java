package APIBookingTickets;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.response.Response;
import org.testng.annotations.Test;

public class BookingTickets {

    public static String authToken; // Store token for reuse

    @Test(priority = 1)
    public void generateToken() {
        Response response =
            given()
                .baseUri("https://restful-booker.herokuapp.com")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "  \"username\": \"admin\",\n" +
                        "  \"password\": \"password123\"\n" +
                        "}")
            .when()
                .post("/auth")
            .then()
                .assertThat().statusCode(200)
                .extract().response();

        authToken = response.jsonPath().getString("token");
        System.out.println("Generated Token: " + authToken);
    }

    @Test(priority = 2)
    public void getBookingById() {
        int bookingId = 1; // You can replace this with any valid ID

        given()
            .baseUri("https://restful-booker.herokuapp.com")
            .header("Content-Type", "application/json")
            
        .when()
            .get("/booking/" + bookingId)
        .then()
            .assertThat().statusCode(200)
            .log().all(); // This prints full response in console
    }
}
