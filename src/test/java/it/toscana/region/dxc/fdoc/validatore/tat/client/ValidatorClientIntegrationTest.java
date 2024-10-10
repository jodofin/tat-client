package it.toscana.region.dxc.fdoc.validatore.tat.client;

import com.fasterxml.jackson.databind.JsonNode;
import it.toscana.region.dxc.fdoc.validatore.tat.client.service.HttpClientService;
import it.toscana.region.dxc.fdoc.validatore.tat.client.service.impl.ApacheHttpClientService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorClientIntegrationTest {
    private static ValidatorClient validatorClient;
    private static HttpClientService httpClientService;

    @BeforeAll
    public static void setup() throws Exception {
        httpClientService = new ApacheHttpClientService();
        validatorClient = new ValidatorClient(httpClientService);
    }

    @Test
    void testCallValidateEndpoints() {
        JsonNode response = validatorClient.callValidateEndpoints();
        assertNotNull(response, "Response should not be null");
        assertTrue(response.isArray(), "Response should be an array");
        assertFalse(response.isEmpty());
        JsonNode firstPost = response.get(0);
        assertNotNull(firstPost.get("id"), "First post should have an id");
        assertNotNull(firstPost.get("title"), "First post should have a title");

    }

    @Test
    void testCallValidatePostEndpoints() {
        JsonNode response = validatorClient.callValidatePostEndpoints();
        assertNotNull(response, "Response from GET request should not be null");
        assertTrue(response.isArray(), "Response should be an array");
        assertFalse(response.isEmpty());
        JsonNode firstPost = response.get(0);
        assertNotNull(firstPost.get("id"), "First post should have an id");
        assertNotNull(firstPost.get("title"), "First post should have a title");
    }

    @AfterAll
    public static void teardown() throws Exception {
        if (httpClientService != null) {
            httpClientService.close();
        }
    }
}
