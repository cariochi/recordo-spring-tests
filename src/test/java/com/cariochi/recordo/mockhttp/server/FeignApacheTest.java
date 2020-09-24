package com.cariochi.recordo.mockhttp.server;

import com.cariochi.recordo.MockHttpServer;
import com.cariochi.recordo.*;
import com.cariochi.recordo.given.Assertion;
import com.cariochi.recordo.mockhttp.server.dto.Gist;
import com.cariochi.recordo.mockhttp.server.dto.GistResponse;
import feign.Client;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@SpringBootTest(
        classes = {RecordoTestsApplication.class, FeignApacheTest.Config.class},
        properties = "feign.httpclient.enabled=true"
)
@ExtendWith(RecordoExtension.class)
class FeignApacheTest {

    @Autowired
    @EnableRecordo
    private HttpClient httpClient;

    @Autowired
    protected GitHub gitHub;

    @Test
    @MockHttpServer("/mockhttp/feign-apache/should_retrieve_gists.rest.json")
    void should_retrieve_gists(
            @Given("/mockhttp/gists.json") Assertion<List<GistResponse>> assertion
    ) {
        assertion.assertAsExpected(gitHub.getGists());
    }

    @Test
    @MockHttpServer("/mockhttp/feign-apache/should_create_gist.rest.json")
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
    @ConditionalOnProperty("feign.httpclient.enabled")
    @EnableFeignClients
    public static class Config {

        @Bean
        public CloseableHttpClient httpClient() {
            return HttpClients.createDefault();
        }

        @Bean
        public Client feignClient() {
            return new feign.httpclient.ApacheHttpClient(httpClient());
        }
    }
}
