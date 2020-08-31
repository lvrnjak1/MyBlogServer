package ba.unsa.etf.zavrsni.app.rest.responses;

import ba.unsa.etf.zavrsni.app.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String bio;
    private boolean isFollowedByLoggedInAccount;

    public AccountResponse(Account account, boolean isFollowedByLoggedInAccount) {
        this.id = account.getId();
        this.name = account.getName();
        this.surname = account.getSurname();
        this.username = account.getUser().getUsername();
        this.email = account.getUser().getEmail();
        this.bio = account.getBio();
        this.isFollowedByLoggedInAccount = isFollowedByLoggedInAccount;
    }
}
