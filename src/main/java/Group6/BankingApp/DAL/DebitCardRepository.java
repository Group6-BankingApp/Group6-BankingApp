package Group6.BankingApp.DAL;

import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.DebitCard;
import Group6.BankingApp.Models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DebitCardRepository extends CrudRepository<DebitCard, Long> {

    @Override
    Iterable<DebitCard> findAll();

    @Override
    Optional<DebitCard> findById(Long id);

    @Override
    <D extends DebitCard> D save(D entity);

    @Override
    void deleteById(Long id);

    DebitCard findByUuid(String uuid);

    DebitCard findByAccountAndIsActive(Account account, boolean isActive);

    DebitCard findByUuidAndIsActive(String uuid, boolean isActive);
}
