package org.j2os.api.dto;

import org.j2os.entity.Account;

import java.util.UUID;

public record AccountResponse(
        UUID accountId,
        Long accountBalance,
        String accountOwnerName,
        String accountOwnerMail,
        Long version
) {
    public static AccountResponse from(Account account){
        return new AccountResponse(
          account.getAccountId(),
                account.getAccountBalance(),
                account.getAccountOwnerName(),
                account.getAccountOwnerMail(),
                account.getVersion()
        );
    }
}
