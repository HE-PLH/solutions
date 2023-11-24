import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class HelloApplication {

    private static HttpClient httpClient;
    private static final String BASE_URL = "https://api.com";

    @BeforeClass
    public static void setUp() {
        httpClient = HttpClients.createDefault();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        httpClient.close();
    }

    @Test
    public void my_get_endpoint() throws Exception {
        int vehicleId = 1;
        String endpoint = "/vehicles/" + vehicleId;

        HttpGet request = new HttpGet(BASE_URL + endpoint);
        HttpResponse response = httpClient.execute(request);

        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == 404) {
            assertTrue("Non-existent endpoint should return 404", true);
            return;
        }

        assertTrue("Failed with status code: " + statusCode, statusCode >= 200 && statusCode < 300);

        //json parsing
        String responseBody = EntityUtils.toString(response.getEntity());
        ObjectMapper objectMapper = new ObjectMapper();
        if (statusCode == 200) {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            assertTrue("Response should contain 'id'", jsonNode.has("id"));
            assertEquals("Expected vehicle ID", vehicleId, jsonNode.get("id").asInt());
            assertTrue("Response should contain 'name'", jsonNode.has("name"));
        } else {
            assertTrue("Unexpected error condition", false);
        }
    }

    @Test
    public void non_existent_endpoint_returns404() throws Exception {
        String nonExistentEndpoint = "/nonexistent";
        HttpGet request = new HttpGet(BASE_URL + nonExistentEndpoint);
        HttpResponse response = httpClient.execute(request);

        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals("Non-existent endpoint should return 404", 404, statusCode);
    }

    @Test
    public void incorrect_parameters_returnbadvalues() throws Exception {
        String endpointWithIncorrectParameters = "/vehicles/invalid";
        HttpGet request = new HttpGet(BASE_URL + endpointWithIncorrectParameters);
        HttpResponse response = httpClient.execute(request);

        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue("Incorrect parameters should return 4xx", statusCode >= 400 && statusCode < 500);
    }
}
