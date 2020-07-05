package ba.unsa.etf.zavrsni.app.resolver;

import ba.unsa.etf.zavrsni.app.model.User;
import ba.unsa.etf.zavrsni.app.services.UserService;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Query implements GraphQLQueryResolver {
    private final UserService userService;

    public Query(UserService userService) {
        this.userService = userService;
    }

    public List<User> getUsers(){
        return userService.getAllUsers();
    }
}
