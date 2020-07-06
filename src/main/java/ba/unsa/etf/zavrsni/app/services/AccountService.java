package ba.unsa.etf.zavrsni.app.services;

import ba.unsa.etf.zavrsni.app.input.AccountInput;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.User;
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
        Account account = accountInput.castToAccount();
        User user = account.getUser();
        userService.save(user);
        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
