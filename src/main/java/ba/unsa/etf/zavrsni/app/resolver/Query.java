package ba.unsa.etf.zavrsni.app.resolver;

import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Post;
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

    public Account getAccount(DataFetchingEnvironment environment){
        return authContext.getSignedInAccount(environment);
    }

    public List<Post> getPostsByFollowing(DataFetchingEnvironment environment){
        return postService.getAllPostsByFollowing(
                authContext.getSignedInAccount(environment)
        );
    }

    public Account getAccountById(Long accountId){
        return accountService.getAccountById(accountId);
    }

    public Post getPostById(Long postId){
        return postService.findById(postId);
    }
}
