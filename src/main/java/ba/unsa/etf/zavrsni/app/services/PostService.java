package ba.unsa.etf.zavrsni.app.services;

import ba.unsa.etf.zavrsni.app.exceptions.ResourceNotFoundException;
import ba.unsa.etf.zavrsni.app.input.PostInput;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.repositories.AccountRepository;
import ba.unsa.etf.zavrsni.app.repositories.PostRepository;
import ba.unsa.etf.zavrsni.app.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AccountRepository accountRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post addPost(PostInput postInput) {
        Post post = postInputToPost(postInput);
        return postRepository.save(post);
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
}
