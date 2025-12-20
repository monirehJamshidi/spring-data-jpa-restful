package org.j2os.service;

import lombok.RequiredArgsConstructor;
import org.j2os.api.dto.AccountCreateRequest;
import org.j2os.api.dto.AccountResponse;
import org.j2os.entity.Account;
import org.j2os.exception.ResourceNotFoundException;
import org.j2os.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public AccountResponse createAccount(AccountCreateRequest request){
        Account account = new Account()
                .setAccountBalance(request.accountBalance())
                .setAccountOwnerName(request.accountOwnerName())
                .setAccountOwnerMail(request.accountOwnerMail())
                .setAccountOwnerAddress(request.accountOwnerAddress());

        Account saved = accountRepository.save(account);

        return new AccountResponse(
                saved.getAccountId(),
                saved.getAccountBalance(),
                saved.getAccountOwnerName(),
                saved.getAccountOwnerMail()
        );
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
    public AccountResponse getAccountById(UUID id){
        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account with id " + id + " not found"
                        )
                );
        return new AccountResponse(
                account.getAccountId(),
                account.getAccountBalance(),
                account.getAccountOwnerName(),
                account.getAccountOwnerMail()
        );
    }

    //DELETE
    @Transactional
    public void deleteById(UUID id){
        accountRepository.deleteById(id);
    }

    //UPDATE
    public Account update(String id, Account account){
        Account existng = accountRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Account not found" + id));

        existng.setAccountBalance(account.getAccountBalance());
        existng.setAccountOwnerName(account.getAccountOwnerName());
        existng.setAccountOwnerMail(account.getAccountOwnerMail());
        existng.setAccountOwnerAddress(account.getAccountOwnerAddress());
        return accountRepository.save(existng);
    }
}
