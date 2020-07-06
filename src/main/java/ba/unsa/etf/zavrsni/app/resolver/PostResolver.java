package ba.unsa.etf.zavrsni.app.resolver;

import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Like;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.services.LikeService;
import com.coxautodev.graphql.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostResolver implements GraphQLResolver<Post> {
    private final LikeService likeService;

    public PostResolver(LikeService likeService) {
        this.likeService = likeService;
    }

    public Account author(Post post){
        return post.getAuthor();
    }

    public List<Like> likes(Post post){
        return likeService.getAllLikesForPost(post);
    }

    public String dateTimePosted(Post post){
        return String.valueOf(post.getDateTimePosted().getTime());
    }
}
