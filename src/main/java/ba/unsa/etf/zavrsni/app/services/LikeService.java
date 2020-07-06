package ba.unsa.etf.zavrsni.app.services;

import ba.unsa.etf.zavrsni.app.exceptions.RedundantOperationException;
import ba.unsa.etf.zavrsni.app.exceptions.ResourceNotFoundException;
import ba.unsa.etf.zavrsni.app.input.LikeInput;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Like;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.repositories.AccountRepository;
import ba.unsa.etf.zavrsni.app.repositories.LikeRepository;
import ba.unsa.etf.zavrsni.app.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

    public Like addLike(LikeInput likeInput) {
        Like like = fromLikeInputToLike(likeInput);
        if(likeRepository.findByAccountAndLikedPost(like.getAccount(), like.getLikedPost()).isPresent()){
            throw new RedundantOperationException("This account already liked this post");
        }
        return likeRepository.save(like);
    }

    private Like fromLikeInputToLike(LikeInput likeInput) {
        Account account = accountRepository.findById(likeInput.getAccountId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Nonexistent account")
                );
        Post post = postRepository.findById(likeInput.getPostId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Nonexistent post")
                );
        return new Like(null, account, post);
    }

    public List<Like> getAllLikesForPost(Post post) {
        return likeRepository.findAllByLikedPost(post);
    }
}
