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

    public PostResponse(Post post, boolean likedByCurrentUser) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.body = post.getBody();
        this.dateTimePosted = String.valueOf(post.getDateTimePosted().getTime());
        this.edited = post.isEdited();
        this.likedByTheCurrentUser = likedByCurrentUser;
    }
}
