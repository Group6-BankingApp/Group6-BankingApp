package Group6.BankingApp.DAL;

import Group6.BankingApp.Models.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    @Override
    Iterable<Transaction> findAll();

    @Override
    Optional<Transaction> findById(Long aLong);

}
