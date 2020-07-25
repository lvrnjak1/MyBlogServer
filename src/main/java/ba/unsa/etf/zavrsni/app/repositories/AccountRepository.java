package ba.unsa.etf.zavrsni.app.repositories;

import ba.unsa.etf.zavrsni.app.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUser_Username(String username);
}
