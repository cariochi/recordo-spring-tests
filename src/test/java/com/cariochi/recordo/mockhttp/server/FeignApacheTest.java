package com.cariochi.recordo.mockhttp.server;

import com.cariochi.recordo.*;
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

import static com.cariochi.recordo.assertions.RecordoAssertion.assertAsJson;

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
    @WithMockHttpServer("/mockhttp/feign-apache/should_retrieve_gists.rest.json")
    void should_retrieve_gists() {
        final List<GistResponse> gists = gitHub.getGists();
        assertAsJson(gists)
                .isEqualTo("/mockhttp/gists.json");
    }

    @Test
    @WithMockHttpServer("/mockhttp/feign-apache/should_create_gist.rest.json")
    void should_create_gist(
            @Read("/mockhttp/gist.json") Gist gist
    ) {
        GistResponse response = gitHub.createGist(gist);
        final Gist created = gitHub.getGist(response.getId(), "hello world");
        gitHub.deleteGist(response.getId());
        assertAsJson(created)
                .isEqualTo("/mockhttp/gist.json");
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
        public Client feignClient(HttpClient httpClient) {
            return new feign.httpclient.ApacheHttpClient(httpClient);
        }
    }
}
