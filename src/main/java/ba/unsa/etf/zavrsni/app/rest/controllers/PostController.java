package ba.unsa.etf.zavrsni.app.rest.controllers;

import ba.unsa.etf.zavrsni.app.rest.requests.PostRequest;
import ba.unsa.etf.zavrsni.app.rest.responses.AccountResponse;
import ba.unsa.etf.zavrsni.app.rest.responses.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/rest-api/posts")
@RequiredArgsConstructor
public class PostController {

    //get post by id
    @GetMapping("/{id}")
    public PostResponse getPostById(@PathVariable Long id, Principal principal){
        return null;
    }

    //get post author
    @GetMapping("/{id}/author")
    public AccountResponse getPostAuthor(@PathVariable Long id, Principal principal){
        return null;
    }

    //get post likes
    @GetMapping("/{id}/likes")
    public List<AccountResponse> getPostLikes(@PathVariable Long id, Principal principal){
        return null;
    }

    //add post
    @PostMapping
    public PostResponse addPost(@Valid @RequestBody PostRequest postRequest, Principal principal){
        return null;
    }

    //edit post
    @PutMapping("/{id}")
    public PostResponse editPost(@PathVariable Long id,
                                @Valid @RequestBody PostRequest postRequest,
                                Principal principal){
        return null;
    }

    //delete post
    @PutMapping("/{id}")
    public PostResponse deletePost(@PathVariable Long id, Principal principal){
        return null;
    }

    //toggle like
    @PutMapping("/{id}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long id){
        return null;
    }

    //populateFeed
    @GetMapping
    public List<PostResponse> populateFeed(@RequestParam Integer offsetDays,
                                           @RequestParam Integer numberOfDays,
                                           Principal principal){
        return null;
    }
}
