package ba.unsa.etf.zavrsni.app.output;

import ba.unsa.etf.zavrsni.app.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInPayload {
    private String token;
    private Account account;
}
