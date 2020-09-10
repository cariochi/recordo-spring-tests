package com.cariochi.recordo.mockhttp;

import com.cariochi.recordo.*;
import com.cariochi.recordo.given.Assertion;
import com.cariochi.recordo.mockhttp.dto.Gist;
import com.cariochi.recordo.mockhttp.dto.GistResponse;
import com.cariochi.recordo.mockhttp.resttemplate.GitHubRestTemplate;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootTest(
        classes = {RecordoTestsApplication.class, RestTemplateApacheTest.Config.class},
        properties = "resttemplate.httpclient.enabled=true"
)
@ExtendWith(RecordoExtension.class)
public class RestTemplateApacheTest {

    @Autowired
    @EnableRecordo
    private HttpClient httpClient;

    @Autowired
    protected GitHub gitHub;

    @Test
    @MockHttp("/mockhttp/rest-template-ok-apache/should_retrieve_gists.rest.json")
    void should_retrieve_gists(
            @Given("/mockhttp/gists.json") Assertion<List<GistResponse>> assertion
    ) {
        assertion.assertAsExpected(gitHub.getGists());
    }

    @Test
    @MockHttp("/mockhttp/rest-template-ok-apache/should_create_gist.rest.json")
    void should_create_gist(
            @Given("/mockhttp/gist.json") Gist gist,
            @Given("/mockhttp/gist.json") Assertion<Gist> assertion
    ) {
        GistResponse response = gitHub.createGist(gist);
        final Gist created = gitHub.getGist(response.getId(), "hello world");
        gitHub.deleteGist(response.getId());
        assertion.assertAsExpected(created);
    }

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
