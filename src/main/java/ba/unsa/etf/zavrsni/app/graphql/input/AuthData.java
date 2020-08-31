package ba.unsa.etf.zavrsni.app.graphql.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthData {
    private String username;
    private String password;
}
