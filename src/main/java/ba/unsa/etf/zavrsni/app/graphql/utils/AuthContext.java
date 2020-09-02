package ba.unsa.etf.zavrsni.app.graphql.utils;

import ba.unsa.etf.zavrsni.app.graphql.exceptions.NotAuthorizedException;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.User;
import ba.unsa.etf.zavrsni.app.services.AccountService;
import graphql.schema.DataFetchingEnvironment;
import graphql.servlet.GraphQLContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RequiredArgsConstructor
@Component
public class AuthContext{
    private final AccountService accountService;

    public Account getSignedInAccount(DataFetchingEnvironment environment){
        GraphQLContext graphQLContext = environment.getContext();
        HttpServletRequest request = graphQLContext.getHttpServletRequest().get();
        //Principal principal = request.getUserPrincipal();
        //User user = (User) principal;
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //System.out.println(user);
        if(user == null){
            throw new NotAuthorizedException("You are not logged in");
        }
        Account current = accountService.getAccountByUser(user)
                .orElseThrow(() -> new RuntimeException("Somehting wrong"));
        //System.out.println(current.getUser().getUsername());
        return current;
    }

    public String getSignedInUsername(DataFetchingEnvironment environment){
        GraphQLContext graphQLContext = environment.getContext();
        HttpServletRequest request = graphQLContext.getHttpServletRequest().get();
        Principal principal = request.getUserPrincipal();
        if(principal == null){
            throw new NotAuthorizedException("You are not logged in");
        }

        return principal.getName();
    }
}
