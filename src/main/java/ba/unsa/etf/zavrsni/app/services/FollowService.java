package ba.unsa.etf.zavrsni.app.services;

import ba.unsa.etf.zavrsni.app.exceptions.RedundantOperationException;
import ba.unsa.etf.zavrsni.app.exceptions.ResourceNotFoundException;
import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Follow;
import ba.unsa.etf.zavrsni.app.output.StatusPayload;
import ba.unsa.etf.zavrsni.app.repositories.AccountRepository;
import ba.unsa.etf.zavrsni.app.repositories.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public List<Account> getAllAccountsFollowedBy(Account account) {
        return followRepository.findAllByFollower(account)
                .stream()
                .map(Follow::getFollowee)
                .collect(Collectors.toList());
    }

    public Follow addFollowRelation(Long followeeId, Account follower) {
        Follow follow = castToFollow(followeeId, follower);
        if(followRepository.findByFollowerAndFollowee(follow.getFollower(), follow.getFollowee()).isPresent()){
            throw new RedundantOperationException("Account already followed");
        }
        return followRepository.save(follow);
    }

    private Follow castToFollow(Long followeeId, Account follower) {
        Account followee = accountRepository.findById(followeeId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Invalid followee account")
                );
        return new Follow(null, follower, followee);
    }

    public Object toggleFollow(Long followeeId, Account follower) {
        Optional<Follow> follow = followRepository.findByFollower_IdAndFollowee_Id(follower.getId(), followeeId);
        if(follow.isEmpty()){
            return addFollowRelation(followeeId, follower);
        }

        removeFollow(follow.get());
        return new StatusPayload("Successfully unfollowed this account", "UNFOLLOW", true);
    }

    private void removeFollow(Follow follow) {
        followRepository.delete(follow);
    }
}
