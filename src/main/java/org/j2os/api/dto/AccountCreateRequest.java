package org.j2os.api.dto;

import jakarta.validation.constraints.*;

public record AccountCreateRequest(
        @NotNull(message = "Account balance is required")
        @Positive(message = "Account balance must be zero or positive")
        Long accountBalance,

        @NotBlank(message = "Owner name must not be blank")
        @Size(min = 2, max = 50, message = "Owner name must be between 2 and 50 characters")
        String accountOwnerName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email format is invalid")
        String  accountOwnerMail,

        @NotBlank(message = "Address is required")
        String accountOwnerAddress
) {
}
