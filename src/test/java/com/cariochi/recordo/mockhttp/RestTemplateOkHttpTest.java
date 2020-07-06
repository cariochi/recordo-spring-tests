package com.cariochi.recordo.mockhttp;

import com.cariochi.recordo.EnableRecordo;
import com.cariochi.recordo.RecordoExtension;
import com.cariochi.recordo.RecordoTestsApplication;
import com.cariochi.recordo.given.Given;
import com.cariochi.recordo.mockhttp.dto.Gist;
import com.cariochi.recordo.mockhttp.dto.GistResponse;
import com.cariochi.recordo.mockhttp.resttemplate.GitHubRestTemplate;
import com.cariochi.recordo.verify.Expected;
import com.cariochi.recordo.verify.Verify;
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
    @MockHttp("/mockhttp/rest-template-ok-http/should_retrieve_gists.rest.json")
    void should_retrieve_gists(
            @Verify("/mockhttp/gists.json") Expected<List<GistResponse>> expected
    ) {
        expected.assertEquals(gitHub.getGists());
    }

    @Test
    @MockHttp("/mockhttp/rest-template-ok-http/should_create_gist.rest.json")
    void should_create_gist(
            @Given("/mockhttp/gist.json") Gist gist,
            @Verify("/mockhttp/rest-template-ok-http/gist_response.json") Expected<GistResponse> expectedResponse,
            @Verify("/mockhttp/gist.json") Expected<Gist> expectedGist
    ) {
        GistResponse response = gitHub.createGist(gist);
        final GistResponse updateResponse = gitHub.updateGist(response.getId(), gist);
        final Gist createdGist = gitHub.getGist(response.getId(), "hello world");
        gitHub.deleteGist(response.getId());
        expectedResponse.assertEquals(updateResponse);
        expectedGist.assertEquals(createdGist);
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
