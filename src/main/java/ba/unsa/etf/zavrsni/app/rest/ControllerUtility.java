package ba.unsa.etf.zavrsni.app.rest;

import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.rest.responses.AccountResponse;
import ba.unsa.etf.zavrsni.app.rest.responses.PostResponse;
import ba.unsa.etf.zavrsni.app.services.AccountService;
import ba.unsa.etf.zavrsni.app.services.LikeService;
import ba.unsa.etf.zavrsni.app.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.security.Principal;

@RequiredArgsConstructor
@Component
public class ControllerUtility {

    private final AccountService accountService;
    private final PostService postService;
    private final LikeService likeService;

    public Account getLoggedInAccount(Principal principal){
        return accountService.getAccountByUsername(principal.getName());
    }

    public ResponseEntity<AccountResponse> getResponseEntityFromAccount(Account account,
                                                                         boolean isFollowedByCurrentUser){
        return ResponseEntity.ok().body(new AccountResponse(account, isFollowedByCurrentUser));
    }

    public boolean isFollowedByCurrentUser(Account account, Principal principal) {
        Account currentUser = getLoggedInAccount(principal);
        return accountService.isFollowedByLoggedInAccount(account, currentUser);
    }

    public ResponseEntity<PostResponse> getResponseEntityFromPost(Post post,
                                                                   boolean likedByCurrentUser, int numberOfLikes){
        return ResponseEntity.ok().body(new PostResponse(post, likedByCurrentUser, numberOfLikes));
    }

    public boolean isLikedByCurrentUser(Post post, Principal principal) {
        Account currentUser = getLoggedInAccount(principal);
        return likeService.checkIfPostLikedBy(post, currentUser);
    }
}
