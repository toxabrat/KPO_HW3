package org.example.controller;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.example.dto.AccountDTO;
import org.example.entity.AccountEntity;
import org.example.repository.AccountRepository;
import org.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/balance")
public class AccountController {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long id) {
        Optional<AccountEntity> accountEntity = accountRepository.findByUserId(id);
        
        return accountEntity.map(entity -> {
            AccountDTO dto = new AccountDTO(entity.getUserId(), entity.getAccount());
            return ResponseEntity.ok().body(dto);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDTO> newAccount(@PathVariable Long id) throws IOException {
        AccountEntity accountEntity = AccountEntity.builder()
                .userId(id)
                .account(0L)
                .build();
        AccountDTO result = accountService.uploadAccount(accountEntity);
        return ResponseEntity.ok()
                .body(result);
    }

    @PostMapping("/put/money/{id}")
    public ResponseEntity<AccountDTO> putMoney(@PathVariable Long id, @RequestBody Long amount) {
        try {
            return ResponseEntity.ok()
                    .body(accountService.putMoney(id, amount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/write/off/money/{id}")
    public ResponseEntity<AccountDTO> writeOffMoney(@PathVariable Long id, @RequestBody Long amount) {
        try {
            return ResponseEntity.ok()
                    .body(accountService.writeOffMoney(id, amount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<AccountDTO>> getAllAccount() {
        List<AccountDTO> ans = accountRepository.findAll().stream().map(
                (item) -> new AccountDTO(item.getUserId(), item.getAccount()))
                .toList();
        return ResponseEntity.ok()
                .body(ans);
    }
}
