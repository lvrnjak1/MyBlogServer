package ba.unsa.etf.zavrsni.app.services;

import ba.unsa.etf.zavrsni.app.exceptions.ResourceNotFoundException;
import ba.unsa.etf.zavrsni.app.input.PostInput;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.repositories.AccountRepository;
import ba.unsa.etf.zavrsni.app.repositories.PostRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final AccountRepository accountRepository;

    public PostService(PostRepository postRepository, AccountRepository accountRepository) {
        this.postRepository = postRepository;
        this.accountRepository = accountRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post addPost(PostInput postInput) {
        Post post = postInputToPost(postInput);
        return postRepository.save(post);
    }

    //TODO error handling
    @SneakyThrows
    private Post postInputToPost(PostInput postInput) {
        Account author = accountRepository.findById(postInput.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author with this id doesn't exist"));
        return new Post(null, postInput.getTitle(), postInput.getBody(), author);

    }

    public List<Post> getAllPostsByAccount(Account account) {
        return postRepository.findAllByAuthor(account);
    }
}
