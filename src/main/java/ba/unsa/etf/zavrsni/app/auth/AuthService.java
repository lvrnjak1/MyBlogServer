package ba.unsa.etf.zavrsni.app.auth;

import ba.unsa.etf.zavrsni.app.graphql.exceptions.InvalidCredentialsException;
import ba.unsa.etf.zavrsni.app.model.User;
import ba.unsa.etf.zavrsni.app.repositories.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    //private final RoleService roleService;

    public AuthService(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        //this.roleService = roleService;
    }

    public String authenticate(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);
        if(!userDetails.getPassword().equals(password)){
            throw new InvalidCredentialsException("Invalid credentials");
        }
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        catch (BadCredentialsException e) {
            System.err.println("Bad credentials!");
        }
        return jwtProvider.generateToken(userDetails);
    }

    public User register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) throw new RuntimeException("User exists");
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException("User " + s + " not found"));
    }
}
