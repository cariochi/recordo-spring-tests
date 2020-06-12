package com.cariochi.recordo.feign;

import com.cariochi.recordo.GitHub;
import com.cariochi.recordo.dto.Gist;
import com.cariochi.recordo.dto.GistResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "GitHub", url = "https://api.github.com")
public interface GitHubFeign extends GitHub {

    @GetMapping("/gists")
    List<GistResponse> getGists();

    @GetMapping("/gists/{id}")
    Gist getGist(@PathVariable String id, @RequestParam String rand);

    @PostMapping("/gists")
    GistResponse createGist(Gist gist);

    @PatchMapping("/gists/{id}")
    GistResponse updateGist(@PathVariable String id, Gist gist);

    @DeleteMapping("/gists/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteGist(@PathVariable String id);

}
