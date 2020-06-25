package com.cariochi.recordo.client.resttemplate;

import com.cariochi.recordo.RecordoTestsApplication;
import com.cariochi.recordo.annotation.EnableRecordo;
import com.cariochi.recordo.annotation.Resources;
import com.cariochi.recordo.client.AbstractTest;
import com.cariochi.recordo.client.GitHub;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(
        classes = {RecordoTestsApplication.class, OkHttpRestTemplateTest.Config.class},
        properties = "resttemplate.okhttp.enabled=true"
)
@Resources("/client/rest-template-ok-http")
public class OkHttpRestTemplateTest extends AbstractTest {

    @Autowired
    @EnableRecordo
    private OkHttpClient client;

    @Configuration
    @ConditionalOnProperty("resttemplate.okhttp.enabled")
    public static class Config {

        @Bean
        public OkHttpClient client() {
            return new OkHttpClient();
        }

        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate(new OkHttp3ClientHttpRequestFactory(client()));
        }

        @Bean
        public GitHub gitHub() {
            return new GitHubRestTemplate(restTemplate());
        }
    }

}