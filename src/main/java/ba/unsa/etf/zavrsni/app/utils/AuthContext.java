package ba.unsa.etf.zavrsni.app.utils;

import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.services.AccountService;
import graphql.schema.DataFetchingEnvironment;
import graphql.servlet.GraphQLContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Component
public class AuthContext{
    private final AccountService accountService;

    public Account getSignedInAccount(DataFetchingEnvironment environment){
        GraphQLContext graphQLContext = environment.getContext();
        HttpServletRequest request = graphQLContext.getHttpServletRequest().get();
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return accountService.getAccountByUserId(Long.valueOf(token));
    }
}
