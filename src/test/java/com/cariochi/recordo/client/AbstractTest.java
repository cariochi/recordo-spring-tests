package com.cariochi.recordo.client;

import com.cariochi.recordo.annotation.Given;
import com.cariochi.recordo.annotation.RestMocks;
import com.cariochi.recordo.annotation.Verify;
import com.cariochi.recordo.client.dto.Gist;
import com.cariochi.recordo.client.dto.GistResponse;
import com.cariochi.recordo.junit5.RecordoExtension;
import com.cariochi.recordo.verify.Expected;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@ExtendWith(RecordoExtension.class)
public abstract class AbstractTest {

    @Autowired
    protected GitHub gitHub;

    @Given(value = "/../gist.json")
    private Gist gist;

    @Test
    @RestMocks("/should_retrieve_gists.rest.json")
    void should_retrieve_gists(@Verify("/../gists.json") Expected<List<GistResponse>> expected) {
        expected.assertEquals(gitHub.getGists());
    }

    @Test
    @RestMocks("/should_create_gist.rest.json")
    void should_create_gist() {
        GistResponse response = gitHub.createGist(gist);
        gist = gitHub.getGist(response.getId(), "hello world");
        gitHub.deleteGist(response.getId());
    }
}
