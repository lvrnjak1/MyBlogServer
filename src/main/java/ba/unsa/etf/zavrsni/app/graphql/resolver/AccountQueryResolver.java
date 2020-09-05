package ba.unsa.etf.zavrsni.app.graphql.resolver;

import ba.unsa.etf.zavrsni.app.graphql.exceptions.ResourceNotFoundException;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.repositories.AccountRepository;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

@Component
@RequiredArgsConstructor
public class AccountQueryResolver implements GraphQLQueryResolver {

    private final AccountRepository accountRepository;

    public Account account(Long accountId, DataFetchingEnvironment environment){
        Specification<Account> spec = byId(accountId);
        DataFetchingFieldSelectionSet selectionSet = environment.getSelectionSet();

        if(selectionSet.contains("posts")){
            spec = spec.and(fetchPosts());
        }

        return accountRepository.findOne(spec)
                .orElseThrow(() -> new ResourceNotFoundException("Account doesn't exist"));
    }

    public Account getAccountByUsername(String username, DataFetchingEnvironment environment){
        Specification<Account> spec = byUsername(username);
        DataFetchingFieldSelectionSet selectionSet = environment.getSelectionSet();

        if(selectionSet.contains("posts")){
            spec = spec.and(fetchPosts());
        }

        return accountRepository.findOne(spec)
                .orElseThrow(() -> new ResourceNotFoundException("Account doesn't exist"));
    }

    private Specification<Account> byUsername(String username) {
        return (Specification<Account>) (root, query, builder) -> builder.equal(root.get("user").get("username"), username);
    }

    private Specification<Account> fetchPosts() {
        return (Specification<Account>) (root, query, builder) -> {
            Fetch<Account, Post> f = root.fetch("posts", JoinType.LEFT);
            Join<Account, Post> join = (Join<Account, Post>) f;
            return join.getOn();
        };
    }

    private Specification<Account> byId(Long id) {
        return (Specification<Account>) (root, query, builder) -> builder.equal(root.get("id"), id);
    }

}
