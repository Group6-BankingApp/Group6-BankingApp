package Group6.BankingApp.DAL;

import Group6.BankingApp.Models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Override
    Iterable<User> findAll();

    Iterable<User>findAllByHasCurrentAccountIsTrueAndHasSavingsAccountIsTrue();

    @Override
    Optional<User> findById(Long aLong);

    @Override
    <S extends User> S save(S entity);

    @Override
    void deleteById(Long aLong);

    Optional<User> findByEmail(String username);

    Iterable<User> findAllByHasCurrentAccountIsFalseAndHasSavingsAccountIsFalse();
}
