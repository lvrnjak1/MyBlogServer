package ba.unsa.etf.zavrsni.app.resolver;

import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.model.User;
import ba.unsa.etf.zavrsni.app.services.AccountService;
import ba.unsa.etf.zavrsni.app.services.FollowService;
import ba.unsa.etf.zavrsni.app.services.PostService;
import ba.unsa.etf.zavrsni.app.utils.AuthContext;
import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountResolver implements GraphQLResolver<Account> {
    private final AccountService accountService;
    private final PostService postService;
    private final FollowService followService;
    private final AuthContext authContext;

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
        return followService.getAllAccountsFollowedBy(account);
    }

    public int numberOfFollowers(Account account){
        return accountService.getNumberOfFollowers(account);
    }

    public int numberOfFollowing(Account account){
        return accountService.getNumberOfAccountsFollowedBy(account);
    }

    public boolean isFollowedByLoggedInAccount(Account account, DataFetchingEnvironment environment){
        return accountService.isFollowedByLoggedInAccount(account, authContext.getSignedInAccount(environment));
    }
}
