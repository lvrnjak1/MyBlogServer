package ba.unsa.etf.zavrsni.app.rest.controllers;

import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.rest.ControllerUtility;
import ba.unsa.etf.zavrsni.app.rest.responses.AccountResponse;
import ba.unsa.etf.zavrsni.app.rest.responses.ApiResponse;
import ba.unsa.etf.zavrsni.app.rest.responses.PostResponse;
import ba.unsa.etf.zavrsni.app.services.AccountService;
import ba.unsa.etf.zavrsni.app.services.FollowService;
import ba.unsa.etf.zavrsni.app.services.LikeService;
import ba.unsa.etf.zavrsni.app.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest-api/accounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AccountController {
    private final AccountService accountService;
    private final FollowService followService;
    private final PostService postService;
    private final ControllerUtility controllerUtility;
    private final LikeService likeService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id, Principal principal){
        Account account = null;
        try {
            account = accountService.getAccountById(id);
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Account doesn't exist"));
        }

        boolean isFollowedByCurrentUser = controllerUtility.isFollowedByCurrentUser(account, principal);
        return controllerUtility.getResponseEntityFromAccount(account, isFollowedByCurrentUser,
                accountService.getNumberOfFollowers(account), accountService.getNumberOfAccountsFollowedBy(account));
    }

    @GetMapping("/myAccount")
    public ResponseEntity<AccountResponse> getMyAccount(Principal principal){
        Account account = controllerUtility.getLoggedInAccount(principal);
        return ResponseEntity.ok()
                .body(new AccountResponse(account,
                        false, accountService.getNumberOfFollowers(account),
                        accountService.getNumberOfAccountsFollowedBy(account)));
    }

    @GetMapping
    public ResponseEntity<?> getAccountByUsername(@RequestParam String username, Principal principal){
        Account account = null;
        try {
            account = accountService.getAccountByUsername(username);
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Account doesn't exist"));
        }

        boolean isFollowedByCurrentUser = controllerUtility.isFollowedByCurrentUser(account, principal);
        return ResponseEntity.ok().body(new AccountResponse(account, isFollowedByCurrentUser,
                accountService.getNumberOfFollowers(account),
                accountService.getNumberOfAccountsFollowedBy(account)));
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<?> getFollowersForAccount(@PathVariable Long id, Principal principal){
        Account account = null;
        try {
            account = accountService.getAccountById(id);
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Account doesn't exist"));
        }

        return ResponseEntity.ok().body(
                followService.getAllFollowersForAccount(account)
                .stream()
                .map(account1 -> new AccountResponse(account1,
                        controllerUtility.isFollowedByCurrentUser(account1, principal),
                        accountService.getNumberOfFollowers(account1),
                        accountService.getNumberOfAccountsFollowedBy(account1)))
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/followers")
    public ResponseEntity<?> getFollowersForMe(Principal principal){
        return ResponseEntity.ok()
                .body(followService.
                        getAllFollowersForAccount(controllerUtility.getLoggedInAccount(principal))
                        .stream()
                        .map(account1 -> new AccountResponse(account1,
                                controllerUtility.isFollowedByCurrentUser(account1, principal),
                                accountService.getNumberOfFollowers(account1),
                                accountService.getNumberOfAccountsFollowedBy(account1)))
                        .collect(Collectors.toList())
                );
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<?> getFollowingForAccount(@PathVariable Long id, Principal principal){
        Account account = null;
        try {
            account = accountService.getAccountById(id);
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Account doesn't exist"));
        }

        return ResponseEntity.ok().body(followService.getAllAccountsFollowedBy(account)
                .stream()
                .map(account1 -> new AccountResponse(account1,
                        controllerUtility.isFollowedByCurrentUser(account1, principal),
                        accountService.getNumberOfFollowers(account1),
                        accountService.getNumberOfAccountsFollowedBy(account1)))
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/following")
    public ResponseEntity<?> getFollowingForMe(Principal principal){
        return ResponseEntity.ok()
                .body(followService.getAllAccountsFollowedBy(
                        controllerUtility.getLoggedInAccount(principal))
                        .stream()
                        .map(account1 -> new AccountResponse(account1, true,
                                accountService.getNumberOfFollowers(account1),
                                accountService.getNumberOfAccountsFollowedBy(account1)))
                        .collect(Collectors.toList())
                );
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<?> getPostsForAccount(@PathVariable Long id, Principal principal){
        Account account = null;
        try {
            account = accountService.getAccountById(id);
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Account doesn't exist"));
        }

        return ResponseEntity.ok().body(
                postService.getAllPostsByAccount(account)
                        .stream()
                        .map(post -> new PostResponse(post, controllerUtility.isLikedByCurrentUser(post,
                                principal), likeService.getNumberOfLikes(post)))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> getPostsForMe(Principal principal){
        return ResponseEntity.ok().body(postService
                .getAllPostsByAccount(controllerUtility.getLoggedInAccount(principal))
                .stream()
                .map(post -> new PostResponse(post, controllerUtility.isLikedByCurrentUser(post,
                        principal), likeService.getNumberOfLikes(post)))
                .collect(Collectors.toList())
        );
    }

    @PutMapping("/{id}/follow")
    public ResponseEntity<?> toggleFollow(@PathVariable Long id, Principal principal){
        Account account = null;
        try {
            account = accountService.getAccountById(id);
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Account doesn't exist"));
        }

        followService.toggleFollow(id, controllerUtility.getLoggedInAccount(principal));
        return ResponseEntity.ok().body(new ApiResponse("Follow toggled successfully"));
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String query, Principal principal){
        return ResponseEntity.ok().body(
                accountService
                        .searchForAccounts(query)
                        .stream()
                        .map(account ->
                                new AccountResponse(account,
                                        controllerUtility
                                                .isFollowedByCurrentUser(account, principal),
                                        accountService.getNumberOfFollowers(account),
                                        accountService.getNumberOfAccountsFollowedBy(account)))
                        .collect(Collectors.toList())
        );
    }


}
