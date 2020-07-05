package ba.unsa.etf.zavrsni.app.services;

import ba.unsa.etf.zavrsni.app.input.AccountInput;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.User;
import ba.unsa.etf.zavrsni.app.repositories.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserService userService;

    public AccountService(AccountRepository accountRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }

    public Account createNewAccount(AccountInput accountInput) {
        Account account = accountInput.castToAccount();
        User user = account.getUser();
        userService.save(user);
        return accountRepository.save(account);
    }
}
