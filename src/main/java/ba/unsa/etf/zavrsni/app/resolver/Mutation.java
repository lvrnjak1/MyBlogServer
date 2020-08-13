package ba.unsa.etf.zavrsni.app.resolver;

import ba.unsa.etf.zavrsni.app.input.AccountInput;
import ba.unsa.etf.zavrsni.app.input.AuthData;
import ba.unsa.etf.zavrsni.app.input.PostInput;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Follow;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.output.SignInPayload;
import ba.unsa.etf.zavrsni.app.output.StatusPayload;
import ba.unsa.etf.zavrsni.app.services.AccountService;
import ba.unsa.etf.zavrsni.app.services.FollowService;
import ba.unsa.etf.zavrsni.app.services.LikeService;
import ba.unsa.etf.zavrsni.app.services.PostService;
import ba.unsa.etf.zavrsni.app.utils.AuthContext;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mutation implements GraphQLMutationResolver {
    private final AccountService accountService;
    private final PostService postService;
    private final LikeService likeService;
    private final FollowService followService;
    private final AuthContext authContext;

    public Account createAccount(AccountInput account){
        return accountService.createNewAccount(account);
    }

    public Post addPost(PostInput postInput, DataFetchingEnvironment environment){
        return postService.addPost(postInput, authContext.getSignedInAccount(environment));
    }

    public Post editPost(Long postId, String newTitle, String newBody,  DataFetchingEnvironment environment){
        return postService.editPost(postId, newTitle, newBody, authContext.getSignedInAccount(environment));
    }

    public StatusPayload deletePost(Long postId, DataFetchingEnvironment environment){
        return postService.deletePostByAuthor(postId, authContext.getSignedInAccount(environment));
    }

    public Post toggleLike(Long postId, DataFetchingEnvironment environment){
        return likeService.toggleLikeByAccount(postId, authContext.getSignedInAccount(environment));
    }

    public Follow toggleFollow(Long followeeId, DataFetchingEnvironment environment){
        return followService.toggleFollow(followeeId, authContext.getSignedInAccount(environment));
    }

    public SignInPayload signIn(AuthData authData){
        return accountService.signInUser(authData);
    }
}
