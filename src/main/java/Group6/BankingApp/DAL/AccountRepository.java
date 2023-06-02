package Group6.BankingApp.DAL;

import Group6.BankingApp.Models.*;
import Group6.BankingApp.Models.dto.AccountDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

//    @Override
//    Iterable<Account> findAll();

    @Override
    Optional<Account> findById(String iban);

    Account findByIban(String iban);

    List<Account> findAllBy(Pageable pageable);

    @Override
    <A extends Account> A save(A entity);

    @Override
    void deleteById(String iban);

    @Query(value = "SELECT * FROM account LIMIT :limit OFFSET :skip", nativeQuery = true)
    List<Account> findAllAccounts(Integer skip, Integer limit);

}