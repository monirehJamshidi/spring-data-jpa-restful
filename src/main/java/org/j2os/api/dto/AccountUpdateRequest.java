package org.j2os.api.dto;

import jakarta.validation.constraints.*;

public record AccountUpdateRequest(
        @NotNull(message = "Account balance is required")
        @PositiveOrZero(message = "Account balance must be zero or positive")
        Long accountBalance,

        @NotBlank(message = "Owner name must not be blank")
        @Size(min = 2, max = 50)
        String accountOwnerName,

        @NotBlank(message = "Email is required")
        @Email
        String accountOwnerMail,

        @NotBlank(message = "Address is required")
        String accountOwnerAddress,

        @NotNull(message = "Version is required")
        Long version
) {}