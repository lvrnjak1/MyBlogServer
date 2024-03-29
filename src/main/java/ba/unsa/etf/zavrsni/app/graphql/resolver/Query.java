package ba.unsa.etf.zavrsni.app.graphql.resolver;

import ba.unsa.etf.zavrsni.app.graphql.utils.AuthContext;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.services.AccountService;
import ba.unsa.etf.zavrsni.app.services.PostService;
import ba.unsa.etf.zavrsni.app.services.UserService;
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

    public Post getPostById(Long postId){
        return postService.findById(postId);
    }

    public List<Post> populateFeed(int offsetDays, int numberOfDays, DataFetchingEnvironment environment){
        return postService.getFollowedPostsByOffsetAndNumberOfDays(offsetDays, numberOfDays,
                authContext.getSignedInAccount(environment));
    }

    public List<Account> searchAccounts(String toSearch){
        return accountService.searchForAccounts(toSearch);
    }
}
