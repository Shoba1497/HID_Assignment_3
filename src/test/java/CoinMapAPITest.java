import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;


import java.util.List;
import java.util.Map;

public class CoinMapAPITest {

    public static void main(String[] args) {
        // Set base URL for the API
        RestAssured.baseURI = "https://coinmap.org/api/v1";

       // Make a GET call to the API
        Response response = given()
            .when()
            .get("/venues")
            .then()
            .extract()
            .response();

        // Get the response body as a string
        String responseBody = response.getBody().asString();

        // Print the response body (you can remove this in production)
        System.out.println("Response Body:\n" + responseBody);

        // Count categories
        int atmCount = 0, cafeCount = 0, shoppingCount = 0, foodCount = 0, lodgingCount = 0, attractionCount = 0, defaultCount = 0;

        // Parse the response JSON and count categories
        JsonPath jsonPath = new JsonPath(responseBody);
        List<Map<String, Object>> venues = jsonPath.getList("venues");
        for (Map<String, Object> venue : venues) {
            String category = (String) venue.get("category");
            switch (category) {
                case "atm":
                    atmCount++;
                    break;
                case "cafe":
                    cafeCount++;
                    break;
                case "shopping":
                    shoppingCount++;
                    break;
                case "food":
                    foodCount++;
                    break;
                case "lodging":
                    lodgingCount++;
                    break;
                case "attraction":
                    attractionCount++;
                    break;
                default:
                    defaultCount++;
            }
        }

        // Print category counts
        System.out.println("Categories count:");
        System.out.println("ATM: " + atmCount);
        System.out.println("Cafe: " + cafeCount);
        System.out.println("Shopping: " + shoppingCount);
        System.out.println("Food: " + foodCount);
        System.out.println("Lodging: " + lodgingCount);
        System.out.println("Attraction: " + attractionCount);
        System.out.println("Default: " + defaultCount);

      
        System.out.println("Food category venues:");
		       	
        for (Map<String, Object> venue : venues) {
            if ("food".equals(venue.get("category"))) {
                System.out.println("Name: " + venue.get("name"));

                String geoString = (String) venue.get("geolocation_degrees");

                // Extract latitude and longitude information
                String[] parts = geoString.split(";")[0].split(",");
                String longitudeStr = parts[0].trim();
                String latitudeStr = parts[1].trim();

                // Extract degrees, minutes, and directions
                String[] longitudeComponents = longitudeStr.split("[°'\"NSEW]");
                double longitude = Double.parseDouble(longitudeComponents[0]) +
                                   Double.parseDouble(longitudeComponents[1]) / 60.0 +
                                   Double.parseDouble(longitudeComponents[2]) / 3600.0;
                
                String[] latitudeComponents = latitudeStr.split("[°'\"NSEW]");
                double latitude = Double.parseDouble(latitudeComponents[0]) +
                                  Double.parseDouble(latitudeComponents[1]) / 60.0 +
                                  Double.parseDouble(latitudeComponents[2]) / 3600.0;

                System.out.println("Geo Location: " + latitude + ", " + longitude);
                System.out.println("----------------------");
            }
        }

    }
        	
}
