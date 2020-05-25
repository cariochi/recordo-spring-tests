package com.cariochi.recordo.resttemplate;

import com.cariochi.recordo.GitHub;
import com.cariochi.recordo.dto.Gist;
import com.cariochi.recordo.dto.GistResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static org.springframework.http.RequestEntity.*;

public class GitHubRestTemplate implements GitHub {

    @Value("${github.key}")
    public String key;

    private final RestTemplate restTemplate;

    public GitHubRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<GistResponse> getGists() {
        return restTemplate.exchange(
                get(URI.create("https://api.github.com/gists"))
                        .header("Authorization", "Bearer " + key)
                        .build(),
                new ParameterizedTypeReference<List<GistResponse>>() {}
        ).getBody();
    }

    @Override
    public Gist getGist(String id, String rand) {
        return restTemplate.exchange(
                get(URI.create("https://api.github.com/gists/" + id))
                        .header("Authorization", "Bearer " + key)
                        .build(),
                Gist.class
        ).getBody();
    }

    @Override
    public GistResponse createGist(Gist gist) {
        return restTemplate.exchange(
                post(URI.create("https://api.github.com/gists"))
                        .header("Authorization", "Bearer " + key)
                        .body(gist),
                GistResponse.class
        ).getBody();
    }

    @Override
    public GistResponse updateGist(String id, Gist gist) {
        return restTemplate.exchange(
                patch(URI.create("https://api.github.com/gists/" + id))
                        .header("Authorization", "Bearer " + key)
                        .body(gist),
                GistResponse.class
        ).getBody();
    }

    @Override
    public void deleteGist(String id) {
        restTemplate.exchange(
                delete(URI.create("https://api.github.com/gists/" + id))
                        .header("Authorization", "Bearer " + key)
                        .build(),
                Void.class
        );
    }
}
