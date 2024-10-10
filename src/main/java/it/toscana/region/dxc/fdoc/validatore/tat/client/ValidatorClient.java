package it.toscana.region.dxc.fdoc.validatore.tat.client;

import com.fasterxml.jackson.databind.JsonNode;
import it.toscana.region.dxc.fdoc.validatore.tat.client.service.HttpClientService;
import it.toscana.region.dxc.fdoc.validatore.tat.client.service.impl.ApacheHttpClientService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ValidatorClient {
    private static final Logger logger = Logger.getLogger(ValidatorClient.class.getName());

    private final HttpClientService httpClientService;
    private final Properties properties;

    public ValidatorClient(HttpClientService httpClientService) throws IOException {
        this.httpClientService = httpClientService;
        this.properties = loadProperties();
    }

    private Properties loadProperties() throws IOException {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IOException("Unable to find application.properties");
            }
            props.load(input);
        }
        return props;
    }

    public Properties getProperties() {
        return this.properties;
    }
    public JsonNode callValidatePostEndpoints() {
        String baseUrl = properties.getProperty("api.base.url");
        JsonNode posts = null;

        try {
            posts = httpClientService.sendRequest("/posts", JsonNode.class, baseUrl);
            logger.info("Posts: " + posts);
           String newPostJson = "{\"title\": \"New Post\", \"body\": \"This is a new post\", \"userId\": 1}";
            JsonNode newPostResponse = httpClientService.sendPostRequest("/posts", newPostJson, JsonNode.class, baseUrl);
            logger.info("New Post Response: " + newPostResponse);
        } catch (Exception e) {
            logger.severe("Error calling validate endpoints: " + e.getMessage());
        }
        return posts;
    }
    public JsonNode callValidateEndpoints() {
        String baseUrl = properties.getProperty("api.base.url");
        JsonNode posts = null;

        try {
            // Make requests to the API
            posts = httpClientService.sendRequest("/posts", JsonNode.class, baseUrl);
            logger.info("Posts: " + posts);

            JsonNode post = httpClientService.sendRequest("/posts/1", JsonNode.class, baseUrl);
            logger.info("Post 1: " + post);
        } catch (Exception e) {
            logger.severe("Error calling validate endpoints: " + e.getMessage());
        }
        return posts; // Return the posts response
    }

    public static void main(String[] args) {
        try {
            HttpClientService clientService = new ApacheHttpClientService(); // Use the implementation
            ValidatorClient client = new ValidatorClient(clientService);
            logger.info("Loading properties");
            String trackId = String.format("Property Loaded: with content: %s", client.properties.get("trackId"));
            logger.info(trackId);

            // Call the service method to execute requests
            JsonNode response = client.callValidateEndpoints();
            // You can log or do something with the response here
            logger.info("Response: " + response);
            clientService.close();
        } catch (Exception e) {
            logger.severe("An error occurred while processing the test: " + e.getLocalizedMessage());
        }
    }
}
