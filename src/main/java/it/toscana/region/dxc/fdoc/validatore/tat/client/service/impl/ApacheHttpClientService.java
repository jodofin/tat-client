package it.toscana.region.dxc.fdoc.validatore.tat.client.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.toscana.region.dxc.fdoc.validatore.tat.client.service.HttpClientService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;

public class ApacheHttpClientService implements HttpClientService {
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ApacheHttpClientService() throws Exception {
        this.httpClient = createHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    private CloseableHttpClient createHttpClient() throws Exception {
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial((chain, authType) -> true)
                .build();

        return HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
    }

    @Override
    public <T> T sendRequest(String endpoint, Class<T> responseType, String baseUrl) throws Exception {
        URI uri = URI.create(baseUrl + endpoint);
        HttpUriRequest request = new HttpGet(uri);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return objectMapper.readValue(response.getEntity().getContent(), responseType);
        }
    }
    public <T> T sendPostRequest(String endpoint, String requestBody, Class<T> responseType, String baseUrl) throws IOException {
        URI uri = URI.create(baseUrl + endpoint);
        HttpPost postRequest = new HttpPost(uri);

        postRequest.setEntity(new StringEntity(requestBody));
        postRequest.setHeader("Content-Type", "application/json");

        try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
            return objectMapper.readValue(response.getEntity().getContent(), responseType);
        }
    }
    @Override
    public void close() throws Exception {
        httpClient.close();
    }
}
