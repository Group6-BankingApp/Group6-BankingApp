package Group6.BankingApp.DAL;

import Group6.BankingApp.Models.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

    @Override
    Optional<Account> findById(String iban);

    Account findByIban(String iban);

    @Override
    <A extends Account> A save(A entity);

    @Override
    void deleteById(String iban);

    @Query("SELECT a FROM Account a WHERE a.user.id = ?1")
    List<Account> findAllByUserId(Long userId);

    @Query("SELECT a FROM Account a WHERE a.user.id = ?1 AND a.accountType = 'Current'")
    Account findCurrentAccountByUserId(Long userId);

    @Query("SELECT a FROM Account a WHERE a.user.id = ?1 AND a.accountType = 'Savings'")
    Account findSavingsAccountByUserId(Long userId);
}
