package ba.unsa.etf.zavrsni.app.resolver;

import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.model.User;
import ba.unsa.etf.zavrsni.app.services.AccountService;
import ba.unsa.etf.zavrsni.app.services.PostService;
import ba.unsa.etf.zavrsni.app.services.UserService;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Query implements GraphQLQueryResolver {
    private final UserService userService;
    private final AccountService accountService;
    private final PostService postService;

    public Query(UserService userService, AccountService accountService, PostService postService) {
        this.userService = userService;
        this.accountService = accountService;
        this.postService = postService;
    }

    public List<User> getUsers(){
        return userService.getAllUsers();
    }

    public List<Account> getAccounts(){
        return accountService.getAllAccounts();
    }

    public List<Post> getPosts(){
        return postService.getAllPosts();
    }
}
