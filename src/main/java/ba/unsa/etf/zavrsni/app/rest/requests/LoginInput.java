package ba.unsa.etf.zavrsni.app.rest.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInput {
    private String username;
    private String password;
}
