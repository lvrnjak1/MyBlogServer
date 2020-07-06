package ba.unsa.etf.zavrsni.app.resolver;

import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Like;
import ba.unsa.etf.zavrsni.app.model.Post;
import com.coxautodev.graphql.tools.GraphQLResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeResolver implements GraphQLResolver<Like> {
    public Account account(Like like){
        return like.getAccount();
    }

    public Post post(Like like){
        return like.getLikedPost();
    }
}
