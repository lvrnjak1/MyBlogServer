package ba.unsa.etf.zavrsni.app.resolver;

import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.utils.NewPostPublisher;
import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Subscription implements GraphQLSubscriptionResolver {
    private final NewPostPublisher newPostPublisher;

    public Publisher<Post> newPost(){
        return newPostPublisher.getPublisher();
    }
}
