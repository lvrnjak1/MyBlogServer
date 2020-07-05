package ba.unsa.etf.zavrsni.app.resolver;

import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.model.User;
import ba.unsa.etf.zavrsni.app.services.AccountService;
import ba.unsa.etf.zavrsni.app.services.PostService;
import com.coxautodev.graphql.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountResolver implements GraphQLResolver<Account> {
    private final AccountService accountService;
    private final PostService postService;

    public AccountResolver(AccountService accountService, PostService postService) {
        this.accountService = accountService;
        this.postService = postService;
    }

    public User user(Account account){
        return  account.getUser();
    }

    public List<Post> posts(Account account){
        return postService.getAllPostsByAccount(account);
    }
}
