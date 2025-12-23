package org.j2os.service;

import lombok.RequiredArgsConstructor;
import org.j2os.api.dto.AccountCreateRequest;
import org.j2os.api.dto.AccountResponse;
import org.j2os.api.dto.AccountUpdateRequest;
import org.j2os.entity.Account;
import org.j2os.exception.PreconditionFailedException;
import org.j2os.exception.ResourceNotFoundException;
import org.j2os.repository.AccountRepository;
import org.j2os.repository.spec.AccountSpecifications;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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


//        return new AccountResponse(
//                saved.getAccountId(),
//                saved.getAccountBalance(),
//                saved.getAccountOwnerName(),
//                saved.getAccountOwnerMail(),
//        );

        return AccountResponse.from(saved);
    }

    // READ all with optional filters
    public Page<AccountResponse> getAccounts(Long balanceMin, String email, Pageable pageable){

//        if (balanceMin != null && email != null){
//            return accountRepository.findAccountsByAccountBalanceGreaterThanAndAccountOwnerMail(balanceMin, email);
//        } else if (balanceMin != null){
//            return accountRepository.findAccountsByAccountBalanceGreaterThan(balanceMin);
//        } else if (email != null){
//            return accountRepository.findByEmail(email);
//        } else {
//            return accountRepository.findAll(pageable);
//        }

        Specification<Account> spec = Specification
                .where(AccountSpecifications.hasMinBalance(balanceMin))
                .and(AccountSpecifications.hasEmail(email));

        return accountRepository.findAll(spec, pageable)
                .map(AccountResponse::from);


    }

    //READ by ID
    public AccountResponse getAccountById(UUID id){
        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account with id " + id + " not found"
                        )
                );
//        return new AccountResponse(
//                account.getAccountId(),
//                account.getAccountBalance(),
//                account.getAccountOwnerName(),
//                account.getAccountOwnerMail()
//        );

        return AccountResponse.from(account);
    }

    //DELETE
    @Transactional
    public void deleteById(UUID id){
        Account account = accountRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Account with id " + id + " not found"
                                )
                        );
        accountRepository.delete(account);
    }

    //UPDATE
    public AccountResponse updateAccount(
            UUID id,
            String ifMatch,
            AccountUpdateRequest request){
        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account with " + id + " not found"
                        )
                );

        Long clientVersion = Long.valueOf(ifMatch.replace("\"", ""));


        // optimistic locking check
        if (account.getVersion() != null && !account.getVersion().equals(clientVersion)){
            throw new PreconditionFailedException( //OptimisticLockingFailureException(
                    "Account was modified by another transaction"
            );
        }

        account.setAccountBalance(request.accountBalance());
        account.setAccountOwnerName(request.accountOwnerName());
        account.setAccountOwnerMail(request.accountOwnerMail());
        account.setAccountOwnerAddress(request.accountOwnerAddress());

        Account updated = accountRepository.save(account);

//        return new AccountResponse(
//                updated.getAccountId(),
//                updated.getAccountBalance(),
//                updated.getAccountOwnerName(),
//                updated.getAccountOwnerMail()
//        );

        return AccountResponse.from(updated);
    }
}
