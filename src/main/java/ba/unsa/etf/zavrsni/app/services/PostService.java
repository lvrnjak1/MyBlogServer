package ba.unsa.etf.zavrsni.app.services;

import ba.unsa.etf.zavrsni.app.exceptions.ResourceNotFoundException;
import ba.unsa.etf.zavrsni.app.input.PostInput;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.output.StatusPayload;
import ba.unsa.etf.zavrsni.app.repositories.AccountRepository;
import ba.unsa.etf.zavrsni.app.repositories.PostRepository;
import ba.unsa.etf.zavrsni.app.utils.DateUtil;
import ba.unsa.etf.zavrsni.app.utils.NewPostPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
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

    public Post addPost(PostInput postInput, Account signedInAccount) {
        Post post =  postRepository.save( postInputToPost(postInput, signedInAccount));
        newPostPublisher.publish(post);
        return post;
    }

    private Post postInputToPost(PostInput postInput, Account author) {
        return new Post(null, postInput.getTitle(), postInput.getBody(),
                DateUtil.parseTimestamp(postInput.getDateTimePosted()), author);

    }

    public List<Post> getAllPostsByAccount(Account account) {
        return postRepository.findAllByAuthor(account);
    }

    private List<Post> getAllPostsByFollowedAccounts(Account currentUser) {
        return followService.getAllAccountsFollowedBy(currentUser)
                .stream()
                .map(this::getAllPostsByAccount)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public Post editPost(Long postId, String newTitle, String newBody, Account signedInAccount) {
        Post post = postRepository.findByIdAndAuthor_Id(postId, signedInAccount.getId())
                .orElseThrow(
                () -> new ResourceNotFoundException("Post doesn't exist")
        );
        post.setTitle(newTitle);
        post.setBody(newBody);
        return postRepository.save(post);
    }

    public StatusPayload deletePostByAuthor(Long postId, Account signedInAccount) {
        Post post = postRepository.findByIdAndAuthor_Id(postId, signedInAccount.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Post doesn't exist")
                );

        postRepository.delete(post);
        return new StatusPayload("Successfully deleted the post", "DELETE_POST", true);
    }

    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Post with this id doesn't exist")
                );
    }

    public List<Post> getFollowedPostsByOffsetAndNumberOfDays(int offsetDays, int numberOfDays, Account account) {
        //gets all posts that are not older than numberOfDays from
        //offsetDays ago
        return getAllPostsByFollowedAccounts(account)
                .stream()
                .filter(post -> postDateInRange(post, offsetDays, numberOfDays))
                .sorted(Comparator.comparing(post -> post.getDateTimePosted().toLocalDateTime()))
                .collect(Collectors.toList());
    }

    private boolean postDateInRange(Post post, int offsetDays, int numberOfDays) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime to = today.minusDays(offsetDays);
        LocalDateTime from = to.minusDays(numberOfDays);
        return isBetween(from, to, post.getDateTimePosted().toLocalDateTime());
    }

    private static boolean isBetween(LocalDateTime dateFrom, LocalDateTime dateTo, LocalDateTime date) {
        return (date.toLocalDate().isAfter(dateFrom.toLocalDate()) || date.toLocalDate().equals(dateFrom.toLocalDate()))
                && (date.toLocalDate().isBefore(dateTo.toLocalDate()) || date.toLocalDate().equals(dateTo.toLocalDate()));
    }
}
