package ba.unsa.etf.zavrsni.app.rest.responses;

import ba.unsa.etf.zavrsni.app.model.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String body;
    private String dateTimePosted;
    private  boolean edited;
    private boolean likedByTheCurrentUser;
    private int numberOfLikes;

    public PostResponse(Post post, boolean likedByCurrentUser, int numberOfLikes) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.body = post.getBody();
        this.dateTimePosted = String.valueOf(post.getDateTimePosted().getTime());
        this.edited = post.isEdited();
        this.likedByTheCurrentUser = likedByCurrentUser;
        this.numberOfLikes = numberOfLikes;
    }
}
