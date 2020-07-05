package ba.unsa.etf.zavrsni.app.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeInput {
    private Long accountId;
    private Long postId;
}
