package ba.unsa.etf.zavrsni.app.services;

import ba.unsa.etf.zavrsni.app.input.FollowInput;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Follow;
import ba.unsa.etf.zavrsni.app.repositories.AccountRepository;
import ba.unsa.etf.zavrsni.app.repositories.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final AccountRepository accountRepository;

    public List<Account> getAllFollowersForAccount(Account account) {
        return followRepository.findAllByFollowee(account)
                .stream()
                .map(Follow::getFollower)
                .collect(Collectors.toList());
    }

    public List<Account> getAllAccountFollowedBy(Account account) {
        return followRepository.findAllByFollower(account)
                .stream()
                .map(Follow::getFollowee)
                .collect(Collectors.toList());
    }

    public Follow addFollowRelation(FollowInput followInput) {
        return followRepository.save(fromFollowInputToFollow(followInput));
    }

    private Follow fromFollowInputToFollow(FollowInput followInput) {
        //TODO error handling
        Account follower = accountRepository.findById(followInput.getFollowerId())
                .orElseThrow();
        Account followee = accountRepository.findById(followInput.getFolloweeId())
                .orElseThrow();
        return new Follow(null, follower, followee);
    }
}
