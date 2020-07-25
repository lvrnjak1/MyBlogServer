package ba.unsa.etf.zavrsni.app.services;

import ba.unsa.etf.zavrsni.app.exceptions.ResourceAlreadyInUse;
import ba.unsa.etf.zavrsni.app.exceptions.ResourceNotFoundException;
import ba.unsa.etf.zavrsni.app.input.AccountInput;
import ba.unsa.etf.zavrsni.app.input.AuthData;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.User;
import ba.unsa.etf.zavrsni.app.output.SignInPayload;
import ba.unsa.etf.zavrsni.app.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserService userService;

    public Account createNewAccount(AccountInput accountInput) {
        checkUsernameAvailability(accountInput.getUser().getUsername());
        checkEmailUsage(accountInput.getUser().getEmail());
        Account account = accountInput.castToAccount();
        User user = account.getUser();
        //userService.encodePWAndSave(user);
        return accountRepository.save(account);
    }

    private void checkUsernameAvailability(String username) {
        if(userService.findByUsername(username).isPresent()){
            throw new ResourceAlreadyInUse("This username is not available");
        }
    }

    private void checkEmailUsage(String email) {
        if(userService.findByEmail(email).isPresent()){
            throw new ResourceAlreadyInUse("Email already in use");
        }
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public SignInPayload signInUser(AuthData authData) {
        return userService.authenticateUser(authData);
    }

    private Account findByUsername(String username) {
        return accountRepository.findByUser_Username(username)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Account with this username doesn't exist")
                );
    }
}
