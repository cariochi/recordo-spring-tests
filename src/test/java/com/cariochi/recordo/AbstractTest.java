package com.cariochi.recordo;

import com.cariochi.recordo.annotation.Given;
import com.cariochi.recordo.annotation.HttpMock;
import com.cariochi.recordo.annotation.Verify;
import com.cariochi.recordo.dto.Gist;
import com.cariochi.recordo.dto.GistResponse;
import com.cariochi.recordo.junit5.RecordoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@ExtendWith(RecordoExtension.class)
public abstract class AbstractTest {

    @Autowired
    protected GitHub gitHub;

    private Gist gist;
    private List<GistResponse> responses;

    @Test
    @HttpMock
    @Verify("responses")
    void should_retrieve_gists() {
        responses = gitHub.getGists();
    }

    @Test
    @Given(value = "gist", file = "/gist.json")
    @HttpMock
    void should_create_gist() {
        GistResponse response = gitHub.createGist(gist);
        gist = gitHub.getGist(response.getId(), "hello world");
        gitHub.deleteGist(response.getId());
    }
}
