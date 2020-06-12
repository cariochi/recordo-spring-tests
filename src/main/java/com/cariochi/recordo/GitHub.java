package com.cariochi.recordo;

import com.cariochi.recordo.dto.Gist;
import com.cariochi.recordo.dto.GistResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GitHub {

    List<GistResponse> getGists();

    Gist getGist(@PathVariable String id, @RequestParam String rand);

    GistResponse createGist(Gist gist);

    GistResponse updateGist(@PathVariable String id, Gist gist);

    void deleteGist(@PathVariable String id);
}
