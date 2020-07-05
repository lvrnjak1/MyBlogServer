package ba.unsa.etf.zavrsni.app.resolver;

import ba.unsa.etf.zavrsni.app.input.AccountInput;
import ba.unsa.etf.zavrsni.app.input.LikeInput;
import ba.unsa.etf.zavrsni.app.input.PostInput;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Like;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.services.AccountService;
import ba.unsa.etf.zavrsni.app.services.LikeService;
import ba.unsa.etf.zavrsni.app.services.PostService;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

@Component
public class Mutation implements GraphQLMutationResolver {
    private final AccountService accountService;
    private final PostService postService;
    private final LikeService likeService;

    public Mutation(AccountService accountService, PostService postService, LikeService likeService) {
        this.accountService = accountService;
        this.postService = postService;
        this.likeService = likeService;
    }

    public Account createAccount(AccountInput accountInput){
        return accountService.createNewAccount(accountInput);
    }

    public Post addPost(PostInput postInput){
        return postService.addPost(postInput);
    }

    public Like addLike(LikeInput likeInput){
        return likeService.addLike(likeInput);
    }
}
