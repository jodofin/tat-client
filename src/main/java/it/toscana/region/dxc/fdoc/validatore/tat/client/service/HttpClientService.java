package it.toscana.region.dxc.fdoc.validatore.tat.client.service;

import java.io.IOException;

public interface HttpClientService {
     <T> T sendPostRequest(String endpoint, String requestBody, Class<T> responseType, String baseUrl) throws IOException;
    <T> T sendRequest(String endpoint, Class<T> responseType, String baseUrl) throws Exception;
    void close() throws Exception;
}
