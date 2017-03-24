import static io.restassured.RestAssured.*;
import io.restassured.RestAssured;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;


/**
 * Created by tmaher on 11/14/16.
 * Based on https://github.com/basdijkstra/workshops/blob/master/rest-assured/RestAssuredWorkshop
 *
 * Need to connect to the MBTA API manually?
 * http://realtime.mbta.com/developer/api/v2/routes?api_key=wX9NwuHnZU2ToO7GmGR9uw&format=json
 */

public class MbtaRoutesTest {

    @BeforeClass
    public static void initPath() {
        RestAssured.baseURI = "http://realtime.mbta.com/developer/api/v2/";
    }

    @Test
    public void checkResponseContentTypeIsJson() {
        given().
                param("api_key","wX9NwuHnZU2ToO7GmGR9uw").
                param("format","json").
        when().get("routes").
        then().assertThat().contentType("application/json");
    }

    @Test
    public void checkResponseCodeIsOk200() {
        given().
                param("api_key","wX9NwuHnZU2ToO7GmGR9uw").
                param("format","json").
        when().get("routes").
        then().assertThat().statusCode(200);
    }

    @Test
    public void checkUserCannotAccessApiWithoutKey() {
        given().
                param("api_key","invalid-key").
                param("format","json").
        when().get("routes").
        then().assertThat().statusCode(401);
    }


    @Test
    public void checkResponseTimeIsLessThan2seconds() {
        given().
                param("api_key","wX9NwuHnZU2ToO7GmGR9uw").
                param("format","json").
        when().get("routes").
        then().assertThat().time(lessThan(2000L), TimeUnit.MILLISECONDS);
    }

    @Test
    public void checkThatRoute230isListed() {
        given().
                param("api_key","wX9NwuHnZU2ToO7GmGR9uw").
                param("format","json").
        when().get("routes").
        then().assertThat().
                statusCode(200).
                body("mode.mode_name", hasItems("Bus"),
                        "mode[3].route.route_name", hasItem("230"));;
    }

}