package ba.unsa.etf.zavrsni.app.resolver;

import ba.unsa.etf.zavrsni.app.input.AccountInput;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.services.AccountService;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

@Component
public class Mutation implements GraphQLMutationResolver {
    private final AccountService accountService;

    public Mutation(AccountService accountService) {
        this.accountService = accountService;
    }

    public Account createAccount(AccountInput accountInput){
        return accountService.createNewAccount(accountInput);
    }
}
