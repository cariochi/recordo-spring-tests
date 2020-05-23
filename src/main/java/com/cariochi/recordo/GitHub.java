package com.cariochi.recordo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "GitHub", url = "https://api.github.com")
public interface GitHub {

    @GetMapping("/gists")
    List<GitHubResponse> getGists();

    @GetMapping("/gists/{id}")
    Gist getGist(@PathVariable String id, @RequestParam String rand);

    @PostMapping("/gists")
    GitHubResponse createGist(Gist gist);

    @PatchMapping("/gists/{id}")
    GitHubResponse updateGist(@PathVariable String id, Gist gist);

    @DeleteMapping("/gists/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteGist(@PathVariable String id);

}
