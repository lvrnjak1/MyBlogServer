package ba.unsa.etf.zavrsni.app.repositories;

import ba.unsa.etf.zavrsni.app.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
    Optional<Account> findByUser_Username(String username);
    Optional<Account> findByUser_Id(Long id);
}
