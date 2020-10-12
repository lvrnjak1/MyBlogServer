package ba.unsa.etf.zavrsni.app.graphql.resolver;

import ba.unsa.etf.zavrsni.app.graphql.utils.AuthContext;
import ba.unsa.etf.zavrsni.app.graphql.utils.NewPostPublisher;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.services.AccountService;
import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Subscription implements GraphQLSubscriptionResolver {
    private final NewPostPublisher newPostPublisher;
    private final AuthContext authContext;
    private final AccountService accountService;

    public Publisher<Post> newPost(DataFetchingEnvironment environment){
        //currently not working with apollo client
        //works if proper token is sent in the header
        return newPostPublisher
                .getPublisher(authContext.getSignedInAccount(environment));
    }
}
