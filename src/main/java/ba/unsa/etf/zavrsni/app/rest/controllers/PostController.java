package ba.unsa.etf.zavrsni.app.rest.controllers;

import ba.unsa.etf.zavrsni.app.graphql.input.PostInput;
import ba.unsa.etf.zavrsni.app.model.Like;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.rest.ControllerUtility;
import ba.unsa.etf.zavrsni.app.rest.requests.EditPostRequest;
import ba.unsa.etf.zavrsni.app.rest.requests.PostRequest;
import ba.unsa.etf.zavrsni.app.rest.responses.AccountResponse;
import ba.unsa.etf.zavrsni.app.rest.responses.ApiResponse;
import ba.unsa.etf.zavrsni.app.rest.responses.PostResponse;
import ba.unsa.etf.zavrsni.app.services.AccountService;
import ba.unsa.etf.zavrsni.app.services.LikeService;
import ba.unsa.etf.zavrsni.app.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest-api/posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PostController {

    private final PostService postService;
    private final LikeService likeService;
    private final ControllerUtility controllerUtility;
    private final AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id, Principal principal){
        Post post = null;
        try {
            post = postService.findById(id);
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Post doesn't exist"));
        }
        boolean likedByCurrentUser = controllerUtility.isLikedByCurrentUser(post, principal);
        return controllerUtility.getResponseEntityFromPost(post, likedByCurrentUser, likeService.getNumberOfLikes(post));
    }

    @GetMapping("/{id}/author")
    public ResponseEntity<?> getPostAuthor(@PathVariable Long id, Principal principal){
        Post post = null;
        try {
            post = postService.findById(id);
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Post doesn't exist"));
        }
        return controllerUtility.getResponseEntityFromAccount(post.getAuthor(),
                controllerUtility.isFollowedByCurrentUser(post.getAuthor(), principal),
                accountService.getNumberOfFollowers(post.getAuthor()),
                accountService.getNumberOfAccountsFollowedBy(post.getAuthor()));
    }

    @GetMapping("/{id}/likes")
    public ResponseEntity<?> getPostLikes(@PathVariable Long id, Principal principal){
        Post post = null;
        try {
            post = postService.findById(id);
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Post doesn't exist"));
        }
        return ResponseEntity.ok().body(
                likeService.getAllLikesForPost(post)
                .stream()
                .map(Like::getAccount)
                .map(account -> new AccountResponse(account,
                        controllerUtility.isFollowedByCurrentUser(account, principal),
                        accountService.getNumberOfFollowers(account),
                        accountService.getNumberOfAccountsFollowedBy(account)))
                .collect(Collectors.toList())
        );
    }

    @PostMapping
    public ResponseEntity<?> addPost(@Valid @RequestBody PostRequest postRequest, Principal principal){
        Post post = postService.addPost(
                new PostInput(postRequest.getTitle(), postRequest.getBody(),
                        postRequest.getDateTimePosted()),
                controllerUtility.getLoggedInAccount(principal)
        );
        return controllerUtility.getResponseEntityFromPost(post, false, likeService.getNumberOfLikes(post));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editPost(@PathVariable Long id,
                                                @Valid @RequestBody EditPostRequest postRequest,
                                                Principal principal){
        Post post = null;
        try {
            post = postService.editPost(id,
                    postRequest.getTitle(),
                    postRequest.getBody(),
                    controllerUtility.getLoggedInAccount(principal));
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Post doesn't exist"));
        }

        return controllerUtility.
                getResponseEntityFromPost(post, controllerUtility.isLikedByCurrentUser(post, principal),
                        likeService.getNumberOfLikes(post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, Principal principal){
        try{
            postService.deletePostByAuthor(id, controllerUtility.getLoggedInAccount(principal));
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Post doesn't exist"));
        }

        return ResponseEntity.ok().body(new ApiResponse("Successfully deleted post"));
    }

    @PutMapping("/{id}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long id, Principal principal){
        try {
            likeService.toggleLikeByAccount(id, controllerUtility.getLoggedInAccount(principal));
        }catch (RuntimeException runtimeException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Post doesn't exist"));
        }
        return ResponseEntity.ok().body(new ApiResponse("Like successfully toggled"));
    }

    @GetMapping
    public ResponseEntity<?> populateFeed(@RequestParam Integer offsetDays,
                                           @RequestParam Integer numberOfDays,
                                           Principal principal){
        return ResponseEntity.ok().body(postService.getFollowedPostsByOffsetAndNumberOfDays(offsetDays, numberOfDays,
                controllerUtility.getLoggedInAccount(principal))
                .stream()
                .map(post -> new PostResponse(post,
                        controllerUtility.isLikedByCurrentUser(post, principal), likeService.getNumberOfLikes(post)))
                .collect(Collectors.toList()));
    }
}
