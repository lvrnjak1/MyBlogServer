package ba.unsa.etf.zavrsni.app.services;

import ba.unsa.etf.zavrsni.app.exceptions.ResourceNotFoundException;
import ba.unsa.etf.zavrsni.app.input.PostInput;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.repositories.AccountRepository;
import ba.unsa.etf.zavrsni.app.repositories.PostRepository;
import ba.unsa.etf.zavrsni.app.utils.DateUtil;
import ba.unsa.etf.zavrsni.app.utils.NewPostPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final NewPostPublisher newPostPublisher;
    private final FollowService followService;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post addPost(PostInput postInput) {
        Post post =  postRepository.save( postInputToPost(postInput));
        newPostPublisher.publish(post);
        return post;
    }

    private Post postInputToPost(PostInput postInput) {
        Account author = accountRepository.findById(postInput.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author doesn't exist"));
        return new Post(null, postInput.getTitle(), postInput.getBody(),
                DateUtil.parseTimestamp(postInput.getDateTimePosted()), author);

    }

    public List<Post> getAllPostsByAccount(Account account) {
        return postRepository.findAllByAuthor(account);
    }

    public List<Post> getAllPostsByFollowing(Account currentUser) {
        return followService.getAllAccountFollowedBy(currentUser)
                .stream()
                .map(this::getAllPostsByAccount)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
