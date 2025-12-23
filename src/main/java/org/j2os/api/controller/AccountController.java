package org.j2os.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.j2os.api.dto.AccountCreateRequest;
import org.j2os.api.dto.AccountResponse;
import org.j2os.api.dto.AccountUpdateRequest;
import org.j2os.entity.Account;
import org.j2os.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {
    private final AccountService accountService;

    //CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(@Valid @RequestBody AccountCreateRequest request){
        return accountService.createAccount(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
    }

    //READ all
    @GetMapping
    public ResponseEntity<List<Account>> getAccounts(
            @RequestParam(required = false) Long balanceMin,
            @RequestParam(required = false) String email
    ){
        List<Account> accounts = accountService.getAccounts(balanceMin, email);
        return ResponseEntity.ok(accounts);
    }

    //READ by ID
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable UUID id){
        AccountResponse response = accountService.getAccountById(id);

        return ResponseEntity.ok()
                .eTag("\"" + response.version() + "\"")
                .body(response);
    }

    //DELETE
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable UUID id){
        accountService.deleteById(id);
    }

    //UPDATE(PUT)
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable UUID id,
            @RequestHeader("If-Match") String ifMatch,
            @Valid @RequestBody AccountUpdateRequest request
    ){
        AccountResponse updated = accountService.updateAccount(id, ifMatch, request);

        return ResponseEntity.ok()
                .eTag("\"" + updated.version() + "\"")
                .body(updated);
    }
}
