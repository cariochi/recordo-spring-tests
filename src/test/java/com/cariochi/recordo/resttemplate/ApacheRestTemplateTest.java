package com.cariochi.recordo.resttemplate;

import com.cariochi.recordo.AbstractTest;
import com.cariochi.recordo.GitHub;
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
        properties = "resttemplate.httpclient.enabled=true"
)
public class ApacheRestTemplateTest extends AbstractTest {

    @Autowired
    @EnableHttpMocks
    private HttpClient httpClient;

    @Configuration
    @ConditionalOnProperty("resttemplate.httpclient.enabled")
    public static class Config {

        @Bean
        public CloseableHttpClient httpClient() {
            return HttpClients.createDefault();
        }

        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient()));
        }

        @Bean
        public GitHub gitHub() {
            return new GitHubRestTemplate(restTemplate());
        }

    }
}
