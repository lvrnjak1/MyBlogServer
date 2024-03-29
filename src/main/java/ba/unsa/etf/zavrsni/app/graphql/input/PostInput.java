package ba.unsa.etf.zavrsni.app.graphql.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostInput {
    private String title;
    private String body;
    private String dateTimePosted;
}
