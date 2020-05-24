package com.cariochi.recordo.resttemplate;

import com.cariochi.recordo.Gist;
import com.cariochi.recordo.GitHubResponse;
import com.cariochi.recordo.annotation.Given;
import com.cariochi.recordo.annotation.HttpMock;
import com.cariochi.recordo.annotation.Verify;
import com.cariochi.recordo.junit5.RecordoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static org.springframework.http.RequestEntity.*;

@ExtendWith(RecordoExtension.class)
public abstract class RestTemplateTest {

    private Gist gist;
    private List<GitHubResponse> responses;

    @Value("${github.key}")
    public String key;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    @Verify("responses")
    @HttpMock
    void should_retrieve_gists() {
        responses = restTemplate.exchange(
                get(URI.create("https://api.github.com/gists"))
                        .header("Authorization", "Bearer" + key)
                        .build(),
                new ParameterizedTypeReference<List<GitHubResponse>>() {}
        ).getBody();
    }

    @Test
    @Given(value = "gist", file = "gist.json")
    @HttpMock
    void should_create_gist() {
        GitHubResponse response = restTemplate.exchange(
                post(URI.create("https://api.github.com/gists"))
                        .header("Authorization", "Bearer" + key)
                        .body(gist),
                GitHubResponse.class
        ).getBody();

        gist = restTemplate.exchange(
                get(URI.create("https://api.github.com/gists/" + response.getId()))
                        .header("Authorization", "Bearer" + key)
                        .build(),
                Gist.class
        ).getBody();

        restTemplate.exchange(
                delete(URI.create("https://api.github.com/gists/" + response.getId()))
                        .header("Authorization", "Bearer" + key)
                        .build(),
                Void.class
        );
    }
}
