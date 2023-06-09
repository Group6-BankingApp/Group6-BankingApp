package Group6.BankingApp.DAL;

import Group6.BankingApp.Models.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<Customer, Long> {

    @Override
    Iterable<Customer> findAll();

    Iterable<Customer>findAllByHasAccountIsTrue();

    @Override
    Optional<Customer> findById(Long aLong);

    @Override
    <S extends Customer> S save(S entity);

    @Override
    void deleteById(Long aLong);

    Optional<Customer> findByEmail(String username);

    Iterable<Customer> findAllByHasAccountIsFalse();
}
