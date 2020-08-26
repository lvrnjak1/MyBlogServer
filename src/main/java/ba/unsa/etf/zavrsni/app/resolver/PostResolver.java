package ba.unsa.etf.zavrsni.app.resolver;

import ba.unsa.etf.zavrsni.app.exceptions.NotAuthorizedException;
import ba.unsa.etf.zavrsni.app.exceptions.ResourceNotFoundException;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Like;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.services.AccountService;
import ba.unsa.etf.zavrsni.app.services.LikeService;
import ba.unsa.etf.zavrsni.app.utils.AuthContext;
import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostResolver implements GraphQLResolver<Post> {
    private final LikeService likeService;
    private final AuthContext authContext;
    private final AccountService accountService;

    public Account author(Post post){
        return post.getAuthor();
    }

    public List<Like> likes(Post post){
        return likeService.getAllLikesForPost(post);
    }

    public String dateTimePosted(Post post){
        return String.valueOf(post.getDateTimePosted().getTime());
    }

    public int numberOfLikes(Post post){
        return likeService.getNumberOfLikes(post);
    }

    public boolean likedByTheCurrentUser(Post post, DataFetchingEnvironment environment){
        Account currentUser;
        try {
            currentUser = accountService.getAccountById(10001L);//authContext.getSignedInAccount(environment);
        }catch (ResourceNotFoundException exception){
            throw new NotAuthorizedException("You are not authorized");
        }

        return likeService.checkIfPostLikedBy(post, currentUser);
    }
}
