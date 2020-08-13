package ba.unsa.etf.zavrsni.app.resolver;

import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.services.AccountService;
import ba.unsa.etf.zavrsni.app.utils.AuthContext;
import ba.unsa.etf.zavrsni.app.utils.NewPostPublisher;
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
        return newPostPublisher
                .getPublisher(accountService.getAccountById(10001L));
        //authContext.getSignedInAccount(environment)
    }
}
