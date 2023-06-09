package Group6.BankingApp.DAL;

import Group6.BankingApp.Models.DebitCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DebitCardRepository extends CrudRepository<DebitCard, String> {

    @Override
    Iterable<DebitCard> findAll();

    @Override
    Optional<DebitCard> findById(String cardNumber);

    @Override
    <D extends DebitCard> D save(D entity);

    @Override
    void deleteById(String cardNumber);
}
