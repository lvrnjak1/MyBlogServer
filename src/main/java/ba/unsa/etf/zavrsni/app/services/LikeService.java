package ba.unsa.etf.zavrsni.app.services;

import ba.unsa.etf.zavrsni.app.input.LikeInput;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Like;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.repositories.AccountRepository;
import ba.unsa.etf.zavrsni.app.repositories.LikeRepository;
import ba.unsa.etf.zavrsni.app.repositories.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

    public LikeService(LikeRepository likeRepository,
                       AccountRepository accountRepository,
                       PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.accountRepository = accountRepository;
        this.postRepository = postRepository;
    }

    public Like addLike(LikeInput likeInput) {
        Like like = fromLikeInputToLike(likeInput);
        if(likeRepository.findByAccountAndLikedPost(like.getAccount(), like.getLikedPost()).isPresent()){
            //TODO error handling
        }
        return likeRepository.save(like);
    }

    private Like fromLikeInputToLike(LikeInput likeInput) {
        //TODO error handling
        Account account = accountRepository.findById(likeInput.getAccountId())
                .orElseThrow();
        Post post = postRepository.findById(likeInput.getPostId())
                .orElseThrow();

        return new Like(null, account, post);
    }

    public List<Like> getAllLikesForPost(Post post) {
        return likeRepository.findAllByLikedPost(post);
    }
}
