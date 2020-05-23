package com.cariochi.recordo.resttemplate;

import com.cariochi.recordo.annotation.EnableHttpMocks;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class ApacheRestTemplateTest extends RestTemplateTest {

    @EnableHttpMocks
    private final HttpClient httpClient = HttpClients.createDefault();

    private final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }

}
