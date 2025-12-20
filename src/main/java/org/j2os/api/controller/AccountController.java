package org.j2os.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.j2os.api.dto.AccountCreateRequest;
import org.j2os.api.dto.AccountResponse;
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
    public AccountResponse getAccountById(@PathVariable UUID id){
        return accountService.getAccountById(id);
    }

    //DELETE
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID id){
        accountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //UPDATE(PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(
            @PathVariable("id") String id,
            @RequestBody Account account
    ){
        Account updated = accountService.update(id, account);
        return ResponseEntity.ok(updated);
    }
}
