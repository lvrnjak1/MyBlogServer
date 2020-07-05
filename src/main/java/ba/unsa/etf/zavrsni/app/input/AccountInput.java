package ba.unsa.etf.zavrsni.app.input;

import ba.unsa.etf.zavrsni.app.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountInput {
    private String name;
    private String surname;
    private String bio;
    //private Date dateOfBirth;
    private UserInput user;

    public Account castToAccount() {
        return new Account(null, name, surname, bio, user.castToUser());
    }
}
