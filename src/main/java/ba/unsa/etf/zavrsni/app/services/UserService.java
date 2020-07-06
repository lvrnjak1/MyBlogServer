package ba.unsa.etf.zavrsni.app.services;

import ba.unsa.etf.zavrsni.app.model.User;
import ba.unsa.etf.zavrsni.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User save(User user){
        return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}
