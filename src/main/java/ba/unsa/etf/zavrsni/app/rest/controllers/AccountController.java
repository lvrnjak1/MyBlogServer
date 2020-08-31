package ba.unsa.etf.zavrsni.app.rest.controllers;

import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.rest.responses.AccountResponse;
import ba.unsa.etf.zavrsni.app.rest.responses.ApiResponse;
import ba.unsa.etf.zavrsni.app.services.AccountService;
import ba.unsa.etf.zavrsni.app.services.FollowService;
import ba.unsa.etf.zavrsni.app.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest-api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final FollowService followService;
    private final PostService postService;

    //get account by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id, Principal principal){
        Account account = null;
        try {
            account = accountService.getAccountById(id);
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Account doesn't exist"));
        }

        boolean isFollowedByCurrentUser = isFollowedByCurrentUser(account, principal);
        return ResponseEntity.ok().body(new AccountResponse(account, isFollowedByCurrentUser));
    }

    @GetMapping
    public ResponseEntity<AccountResponse> getMyAccount(Principal principal){
        return ResponseEntity.ok()
                .body(new AccountResponse(getLoggedInAccount(principal),
                        false));
    }

    //get account by username
    @GetMapping("/{username}")
    public ResponseEntity<?> getAccountByUsername(@PathVariable String username, Principal principal){
        Account account = null;
        try {
            account = accountService.getAccountByUsername(username);
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Account doesn't exist"));
        }

        boolean isFollowedByCurrentUser = isFollowedByCurrentUser(account, principal);
        return ResponseEntity.ok().body(new AccountResponse(account, isFollowedByCurrentUser));
    }

    //get followers
    @GetMapping("/{id}/followers")
    public ResponseEntity<?> getFollowersForAccount(@PathVariable Long id){
        Account account = null;
        try {
            account = accountService.getAccountById(id);
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Account doesn't exist"));
        }

        return ResponseEntity.ok().body(followService.getAllFollowersForAccount(account));
    }

    @GetMapping("/followers")
    public ResponseEntity<?> getFollowersForMe(Principal principal){
        return ResponseEntity.ok()
                .body(followService.getAllFollowersForAccount(getLoggedInAccount(principal)));
    }

    //get following
    @GetMapping("/{id}/following")
    public ResponseEntity<?> getFollowingForAccount(@PathVariable Long id){
        Account account = null;
        try {
            account = accountService.getAccountById(id);
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Account doesn't exist"));
        }

        return ResponseEntity.ok().body(followService.getAllAccountsFollowedBy(account));
    }

    @GetMapping("/following")
    public ResponseEntity<?> getFollowingForMe(Principal principal){
        return ResponseEntity.ok()
                .body(followService.getAllAccountsFollowedBy(getLoggedInAccount(principal)));
    }

    //get posts
    @GetMapping("/{id}/posts")
    public ResponseEntity<?> getPostsForAccount(@PathVariable Long id){
        Account account = null;
        try {
            account = accountService.getAccountById(id);
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Account doesn't exist"));
        }

        return ResponseEntity.ok().body(postService.getAllPostsByAccount(account));
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getPostsForMe(Principal principal){
        return ResponseEntity.ok().body(postService.getAllPostsByAccount(getLoggedInAccount(principal)));
    }

    //toggle follow
    @PutMapping("/{id}/follow")
    public ResponseEntity<?> toggleFollow(@PathVariable Long id, Principal principal){
        Account account = null;
        try {
            account = accountService.getAccountById(id);
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Account doesn't exist"));
        }

        followService.toggleFollow(id, getLoggedInAccount(principal));
        return ResponseEntity.ok().body("Like toggled successfully");
    }

    //search
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String query, Principal principal){
        return ResponseEntity.ok().body(
                accountService
                        .searchForAccounts(query)
                        .stream()
                        .map(account ->
                                new AccountResponse(account,
                                        isFollowedByCurrentUser(account, principal)))
                        .collect(Collectors.toList())
        );
    }

    //utility methods
    private Account getLoggedInAccount(Principal principal){
        return accountService.getAccountByUsername(principal.getName());
    }

    private ResponseEntity<AccountResponse> getResponseEntityFromAccount(Account account,
                                                                         boolean isFollowedByCurrentUser){
        return ResponseEntity.ok().body(new AccountResponse(account, isFollowedByCurrentUser));
    }

    private boolean isFollowedByCurrentUser(Account account, Principal principal) {
        Account currentUser = getLoggedInAccount(principal);
        return accountService.isFollowedByLoggedInAccount(account, currentUser);
    }
}
