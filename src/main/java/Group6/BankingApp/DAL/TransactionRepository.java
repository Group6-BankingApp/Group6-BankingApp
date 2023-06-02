package Group6.BankingApp.DAL;

import Group6.BankingApp.Models.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, String> {

    /*@Override
    Iterable<Transaction> findAll();

    @Override
    Optional<Transaction> findById(UUID aLong);*/
    
}
