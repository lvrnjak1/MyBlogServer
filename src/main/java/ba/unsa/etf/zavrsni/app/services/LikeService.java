package ba.unsa.etf.zavrsni.app.services;

import ba.unsa.etf.zavrsni.app.graphql.exceptions.ResourceNotFoundException;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Like;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.repositories.AccountRepository;
import ba.unsa.etf.zavrsni.app.repositories.LikeRepository;
import ba.unsa.etf.zavrsni.app.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

    public Like addLike(Post post, Account account) {
        return likeRepository.save(new Like(null, account, post));
    }

    public List<Like> getAllLikesForPost(Post post) {
        return likeRepository.findAllByLikedPost(post);
    }

    public Post toggleLikeByAccount(Long postId, Account signedInAccount) {
        Post post = postRepository.findById(postId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Post doesn't exist")
                );

        Optional<Like> like = likeRepository.findByAccount_IdAndLikedPost_Id(signedInAccount.getId(), postId);
        if(like.isPresent()){
            likeRepository.delete(like.get());
            return post;
        }

       addLike(post, signedInAccount);
        return post;
    }

    public int getNumberOfLikes(Post post) {
        return likeRepository.countByLikedPost(post);
    }

    public boolean checkIfPostLikedBy(Post post, Account currentUser) {
        return likeRepository.findByAccount_IdAndLikedPost_Id(currentUser.getId(), post.getId()).isPresent();
    }
}
