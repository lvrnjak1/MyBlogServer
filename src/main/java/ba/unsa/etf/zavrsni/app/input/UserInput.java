package ba.unsa.etf.zavrsni.app.input;

import ba.unsa.etf.zavrsni.app.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInput {
    private String username;
    private String password;
    private String email;

    public User castToUser() {
        return new User(null, username, password, email);
    }
}
