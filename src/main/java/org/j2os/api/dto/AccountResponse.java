package org.j2os.api.dto;

import java.util.UUID;

public record AccountResponse(
        UUID accountId,
        Long accountBalance,
        String accountOwnerName,
        String accountOwnerMail
) {
}
