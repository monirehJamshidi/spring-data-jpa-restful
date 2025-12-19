package org.j2os.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.j2os.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findByAccountBalance(Long accountBalance);
    List<Account> findAccountsByAccountBalanceOrAccountOwnerName(Long accountBalance, String accountOwnerName);
    List<Account> findByAccountBalanceLessThan(Long accountBalanceIsLessThan);

    List<Account> findAccountsByAccountBalanceGreaterThan(Long balance);

    List<Account> findAccountsByAccountBalanceGreaterThanAndAccountOwnerMail(Long balance, @NotBlank @Email(message = "{account.owner.mail.email}") String email);

    @Query("SELECT o from Account o where o.accountOwnerMail = :email")
    List<Account> findByEmail(@Param("email") String email);

    @Query("select o from Account o where o.accountBalance> :minBalance")
    List<Account> getAccounts(@Param("minBalance")Long minBalance);
}
