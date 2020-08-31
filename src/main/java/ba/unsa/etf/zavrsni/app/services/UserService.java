package ba.unsa.etf.zavrsni.app.services;

import ba.unsa.etf.zavrsni.app.graphql.exceptions.InvalidCredentialsException;
import ba.unsa.etf.zavrsni.app.graphql.exceptions.ResourceNotFoundException;
import ba.unsa.etf.zavrsni.app.graphql.input.AuthData;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.User;
import ba.unsa.etf.zavrsni.app.graphql.output.SignInPayload;
import ba.unsa.etf.zavrsni.app.repositories.AccountRepository;
import ba.unsa.etf.zavrsni.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public User save(User user){
        return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public SignInPayload authenticateUser(AuthData authData) {
        Account account = accountRepository.findByUser_Username(authData.getUsername())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Account with this username doesn't exist")
                );

        if(!account.getUser().getPassword().equals(authData.getPassword())){
            throw new InvalidCredentialsException("Invalid password");
        }
        return new SignInPayload(String.valueOf(account.getUser().getId()), account);
    }

//    public void encodePWAndSave(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        save(user);
//    }
}
