package com.cariochi.recordo.mockhttp.server;

import com.cariochi.recordo.*;
import com.cariochi.recordo.mockhttp.server.dto.Gist;
import com.cariochi.recordo.mockhttp.server.dto.GistResponse;
import com.cariochi.recordo.mockhttp.server.interceptors.okhttp.OkHttpClientInterceptor;
import feign.Client;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
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
        classes = {RecordoTestsApplication.class, FeignOkHttpTest.Config.class},
        properties = "feign.okhttp.enabled=true"
)
@ExtendWith(RecordoExtension.class)
class FeignOkHttpTest {

    @Autowired
    @EnableRecordo
    private OkHttpClient client;

    @Autowired
    protected GitHub gitHub;

    @Test
    @WithMockHttpServer("/mockhttp/feign-ok-http/should_retrieve_gists.rest.json")
    void should_retrieve_gists() {
        final List<GistResponse> gists = gitHub.getGists();
        assertAsJson(gists)
                .isEqualTo("/mockhttp/gists.json");
    }

    @Test
    void should_create_gist(
            @Read("/mockhttp/gist.json") Gist gist
    ) {
        try (MockHttpServer mockServer =
                     new MockHttpServer("/mockhttp/feign-ok-http/should_create_gist.http.json", new OkHttpClientInterceptor(client))) {

            mockServer.set("gistId", "16d0b491b237960fd5bf3ba503a3d18b");

            final GistResponse response = gitHub.createGist(gist);
            final GistResponse updateResponse = gitHub.updateGist(response.getId(), gist);
            final Gist createdGist = gitHub.getGist(response.getId(), "hello world");
            gitHub.deleteGist(response.getId());

            assertAsJson(updateResponse)
                    .isEqualTo("/mockhttp/feign-ok-http/gist_response.json");

            assertAsJson(createdGist)
                    .isEqualTo("/mockhttp/gist.json");
        }
    }

    @Configuration
    @ConditionalOnProperty("feign.okhttp.enabled")
    @EnableFeignClients
    public static class Config {

        @Bean
        public OkHttpClient client() {
            return new OkHttpClient();
        }

        @Bean
        public Client feignClient(OkHttpClient client) {
            return new feign.okhttp.OkHttpClient(client);
        }
    }

}
