package Group6.BankingApp.DAL;

import Group6.BankingApp.Models.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

    @Override
    Iterable<Account> findAll();

    @Override
    Optional<Account> findById(String iban);

    @Override
    <A extends Account> A save(A entity);

    @Override
    void deleteById(String iban);
}
