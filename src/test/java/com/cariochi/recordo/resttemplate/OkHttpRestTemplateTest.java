package com.cariochi.recordo.resttemplate;

import com.cariochi.recordo.annotation.EnableHttpMocks;
import okhttp3.OkHttpClient;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class OkHttpRestTemplateTest extends RestTemplateTest {

    @EnableHttpMocks
    private final OkHttpClient client = new OkHttpClient();

    private final RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory(client));

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }

}
