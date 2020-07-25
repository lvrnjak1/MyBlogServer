package ba.unsa.etf.zavrsni.app.resolver;

import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.model.User;
import ba.unsa.etf.zavrsni.app.services.AccountService;
import ba.unsa.etf.zavrsni.app.services.PostService;
import ba.unsa.etf.zavrsni.app.services.UserService;
import ba.unsa.etf.zavrsni.app.utils.AuthContext;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Query implements GraphQLQueryResolver {
    private final UserService userService;
    private final AccountService accountService;
    private final PostService postService;
    private final AuthContext authContext;

    public List<User> getUsers(){
        return userService.getAllUsers();
    }

    public List<Account> getAccounts(){
        return accountService.getAllAccounts();
    }

    public List<Post> getPostsByFollowing(DataFetchingEnvironment environment){
        return postService.getAllPostsByFollowing(
                authContext.getSignedInAccount(environment)
        );
    }
}
