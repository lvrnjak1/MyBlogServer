package ba.unsa.etf.zavrsni.app.repositories;

import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findAllByFollowee(Account followee);
    List<Follow> findAllByFollower(Account follower);
    Optional<Follow> findByFollowerAndFollowee(Account follower, Account followee);
    Optional<Follow> findByFollower_IdAndFollowee_Id(Long followerId, Long followeeId);
    int countByFollowee(Account followee);
    int countByFollower(Account follower);
}
