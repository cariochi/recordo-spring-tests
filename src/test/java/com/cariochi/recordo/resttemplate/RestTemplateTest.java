package com.cariochi.recordo.resttemplate;

import com.cariochi.recordo.*;
import com.cariochi.recordo.annotation.Given;
import com.cariochi.recordo.annotation.HttpMock;
import com.cariochi.recordo.annotation.Verify;
import com.cariochi.recordo.junit5.RecordoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static com.cariochi.recordo.GitHubInterceptor.KEY;
import static org.springframework.http.RequestEntity.*;

@ExtendWith(RecordoExtension.class)
public abstract class RestTemplateTest {

    private Gist gist;
    private List<GitHubResponse> responses;

    protected abstract RestTemplate getRestTemplate();

    @Test
    @Verify("responses")
    @HttpMock
    void should_retrieve_gists() {
        responses = getRestTemplate().exchange(
                get(URI.create("https://api.github.com/gists"))
                        .header("Authorization", "token " + KEY)
                        .build(),
                new ParameterizedTypeReference<List<GitHubResponse>>() {}
        ).getBody();
    }

    @Test
    @Given("gist")
    @HttpMock
    void should_create_gist() {
        GitHubResponse response = getRestTemplate().exchange(
                post(URI.create("https://api.github.com/gists"))
                        .header("Authorization", "token " + KEY)
                        .body(gist),
                GitHubResponse.class
        ).getBody();

        gist = getRestTemplate().exchange(
                get(URI.create("https://api.github.com/gists/" + response.getId()))
                        .header("Authorization", "token " + KEY)
                        .build(),
                Gist.class
        ).getBody();

        getRestTemplate().exchange(
                delete(URI.create("https://api.github.com/gists/" + response.getId()))
                        .header("Authorization", "token " + KEY)
                        .build(),
                Void.class
        );
    }
}
