package org.j2os.service;

import lombok.RequiredArgsConstructor;
import org.j2os.entity.Account;
import org.j2os.exception.AccountNotFoundException;
import org.j2os.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.channels.AcceptPendingException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    //CREATE
    public Account save(Account account){
        return accountRepository.save(account);
    }

    // READ all with optional filters
    public List<Account> getAccounts(Long balanceMin, String email){
        if (balanceMin != null && email != null){
            return accountRepository.findAccountsByAccountBalanceGreaterThanAndAccountOwnerMail(balanceMin, email);
        } else if (balanceMin != null){
            return accountRepository.findAccountsByAccountBalanceGreaterThan(balanceMin);
        } else if (email != null){
            return accountRepository.findByEmail(email);
        } else {
            return accountRepository.findAll();
        }
    }

    //READ by ID
    public Optional<Account> findById(UUID id){
        return accountRepository.findById(id);
    }

    //DELETE
    @Transactional
    public void deleteById(UUID id){
        accountRepository.deleteById(id);
    }

    //UPDATE
    public Account update(String id, Account account){
        Account existng = accountRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AccountNotFoundException("Account not found" + id));

        existng.setAccountBalance(account.getAccountBalance());
        existng.setAccountOwnerName(account.getAccountOwnerName());
        existng.setAccountOwnerMail(account.getAccountOwnerMail());
        existng.setAccountOwnerAddress(account.getAccountOwnerAddress());
        return accountRepository.save(existng);
    }
}
