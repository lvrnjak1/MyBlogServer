package ba.unsa.etf.zavrsni.app.rest.responses;

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

}
