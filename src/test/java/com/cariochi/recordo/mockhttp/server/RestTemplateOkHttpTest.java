package com.cariochi.recordo.mockhttp.server;

import com.cariochi.recordo.MockHttpServer;
import com.cariochi.recordo.*;
import com.cariochi.recordo.given.Assertion;
import com.cariochi.recordo.mockhttp.server.dto.Gist;
import com.cariochi.recordo.mockhttp.server.dto.GistResponse;
import com.cariochi.recordo.mockhttp.server.resttemplate.GitHubRestTemplate;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootTest(
        classes = {RecordoTestsApplication.class, RestTemplateOkHttpTest.Config.class},
        properties = "resttemplate.okhttp.enabled=true"
)
@ExtendWith(RecordoExtension.class)
public class RestTemplateOkHttpTest {

    @Autowired
    @EnableRecordo
    private OkHttpClient client;

    @Autowired
    protected GitHub gitHub;

    @Test
    @MockHttpServer("/mockhttp/rest-template-ok-http/should_retrieve_gists.rest.json")
    void should_retrieve_gists(
            @Given("/mockhttp/gists.json") Assertion<List<GistResponse>> assertion
    ) {
        assertion.assertAsExpected(gitHub.getGists());
    }

    @Test
    @MockHttpServer("/mockhttp/rest-template-ok-http/should_create_gist.rest.json")
    void should_create_gist(
            @Given("/mockhttp/gist.json") Gist gist,
            @Given("/mockhttp/rest-template-ok-http/gist_response.json") Assertion<GistResponse> responseAssertion,
            @Given("/mockhttp/gist.json") Assertion<Gist> gistAssertion
    ) {
        GistResponse response = gitHub.createGist(gist);
        final GistResponse updateResponse = gitHub.updateGist(response.getId(), gist);
        final Gist createdGist = gitHub.getGist(response.getId(), "hello world");
        gitHub.deleteGist(response.getId());
        responseAssertion.assertAsExpected(updateResponse);
        gistAssertion.assertAsExpected(createdGist);
    }

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
