package ba.unsa.etf.zavrsni.app.specification;

import ba.unsa.etf.zavrsni.app.model.Account;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class AccountSpecification implements Specification<Account> {
    private final String filter;
    public AccountSpecification(String filter){
        super();
        this.filter = "%" + filter.toUpperCase() + "%";
    }

    @Override
    public Predicate toPredicate(@NotNull Root<Account> root, @NotNull CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.disjunction();
        if(filter != null) {
            Expression<String> rootNameSurname = criteriaBuilder.concat(criteriaBuilder.upper(root.get("name")), " ");
            rootNameSurname = criteriaBuilder.concat(rootNameSurname, criteriaBuilder.upper(root.get("surname")));
            predicate.getExpressions().add(criteriaBuilder.like(rootNameSurname, filter));

            Expression<String> username = criteriaBuilder.upper(root.get("user").get("username"));
            predicate.getExpressions().add(criteriaBuilder.like(username, filter));
        }
        return predicate;
    }
}
