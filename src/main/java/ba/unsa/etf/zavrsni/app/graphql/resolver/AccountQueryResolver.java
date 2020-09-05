package ba.unsa.etf.zavrsni.app.graphql.resolver;

import ba.unsa.etf.zavrsni.app.graphql.exceptions.ResourceNotFoundException;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.model.User;
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

    public Iterable<Account> accounts(DataFetchingEnvironment environment){
        DataFetchingFieldSelectionSet set = environment.getSelectionSet();
        if(set.contains("user") && set.contains("posts")){
            return accountRepository.findAll(fetchPosts().and(fetchUser()));
        }else if(set.contains("user")){
            return accountRepository.findAll(fetchUser());
        }else if(set.contains("posts")){
            return accountRepository.findAll(fetchPosts());
        }

        return accountRepository.findAll();
    }

    public Account account(Long accountId, DataFetchingEnvironment environment){
        Specification<Account> spec = byId(accountId);
        DataFetchingFieldSelectionSet selectionSet = environment.getSelectionSet();
        if(selectionSet.contains("user")){
            spec = spec.and(fetchUser());
        }

        if(selectionSet.contains("posts")){
            spec = spec.and(fetchPosts());
        }

        return accountRepository.findOne(spec)
                .orElseThrow(() -> new ResourceNotFoundException("Account doesn't exist"));
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

    private Specification<Account> fetchUser() {
        return (Specification<Account>) (root, query, builder) -> {
            Fetch<Account, User> f = root.fetch("user", JoinType.LEFT);
            Join<Account, User> join = (Join<Account, User>) f;
            return join.getOn();
        };
    }
}
