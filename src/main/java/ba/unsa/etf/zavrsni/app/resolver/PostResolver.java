package ba.unsa.etf.zavrsni.app.resolver;

import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Like;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.services.LikeService;
import com.coxautodev.graphql.tools.GraphQLResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostResolver implements GraphQLResolver<Post> {
    private final LikeService likeService;

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
        return likes(post).size();
    }
}
