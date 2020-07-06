package ba.unsa.etf.zavrsni.app.resolver;

import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.model.User;
import ba.unsa.etf.zavrsni.app.services.AccountService;
import ba.unsa.etf.zavrsni.app.services.FollowService;
import ba.unsa.etf.zavrsni.app.services.PostService;
import com.coxautodev.graphql.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountResolver implements GraphQLResolver<Account> {
    private final AccountService accountService;
    private final PostService postService;
    private final FollowService followService;

    public AccountResolver(AccountService accountService,
                           PostService postService,
                           FollowService followService) {
        this.accountService = accountService;
        this.postService = postService;
        this.followService = followService;
    }

    public User user(Account account){
        return  account.getUser();
    }

    public List<Post> posts(Account account){
        return postService.getAllPostsByAccount(account);
    }

    public List<Account> followers(Account account){
        //all accounts following this account
        return followService.getAllFollowersForAccount(account);
    }

    public List<Account> following(Account account){
        //all accounts this account is following
        return followService.getAllAccountFollowedBy(account);
    }
}
