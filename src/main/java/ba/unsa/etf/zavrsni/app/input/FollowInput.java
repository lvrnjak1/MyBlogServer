package ba.unsa.etf.zavrsni.app.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowInput {
    private Long followerId;
    private Long followeeId;
}
