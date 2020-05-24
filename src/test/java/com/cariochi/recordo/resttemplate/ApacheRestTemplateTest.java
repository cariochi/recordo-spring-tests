package com.cariochi.recordo.resttemplate;

import com.cariochi.recordo.RecordoTestsApplication;
import com.cariochi.recordo.annotation.EnableHttpMocks;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(
        classes = {RecordoTestsApplication.class, ApacheRestTemplateTest.Config.class},
        properties = "rest-template.httpclient.enabled=true"
)
public class ApacheRestTemplateTest extends RestTemplateTest {

    @Autowired
    @EnableHttpMocks
    private HttpClient httpClient;

    @Configuration
    @ConditionalOnProperty("rest-template.httpclient.enabled")
    public static class Config {

        @Bean
        public CloseableHttpClient httpClient() {
            return HttpClients.createDefault();
        }

        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient()));
        }

    }
}
