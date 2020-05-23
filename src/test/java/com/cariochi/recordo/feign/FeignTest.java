package com.cariochi.recordo.feign;

import com.cariochi.recordo.*;
import com.cariochi.recordo.annotation.Given;
import com.cariochi.recordo.annotation.HttpMock;
import com.cariochi.recordo.annotation.Verify;
import com.cariochi.recordo.junit5.RecordoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@ExtendWith(RecordoExtension.class)
public abstract class FeignTest {

    @Autowired
    protected GitHub gitHub;

    private Gist gist;
    private List<GitHubResponse> responses;

    @Test
    @HttpMock
    @Verify("responses")
    void should_retrieve_gists() {
        responses = gitHub.getGists();
    }

    @Test
    @Given("gist")
    @HttpMock
    void should_create_gist() {
        GitHubResponse response = gitHub.createGist(gist);
        gist = gitHub.getGist(response.getId(), "hello world");
//        response = gitHub.updateGist(response.getId(), gist);
        gitHub.deleteGist(response.getId());
    }
}
