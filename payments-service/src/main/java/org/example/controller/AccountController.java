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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

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

    @Operation(summary = "Получить аккаунт по id", description = "Возвращает аккаунт пользователя по его id.")
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccount(@Parameter(description = "ID пользователя") @PathVariable Long id) {
        Optional<AccountEntity> accountEntity = accountRepository.findByUserId(id);
        
        return accountEntity.map(entity -> {
            AccountDTO dto = new AccountDTO(entity.getUserId(), entity.getAccount());
            return ResponseEntity.ok().body(dto);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Создать новый аккаунт", description = "Создаёт новый аккаунт с балансом 0 для пользователя по id.")
    @PutMapping("/{id}")
    public ResponseEntity<AccountDTO> newAccount(@Parameter(description = "ID пользователя") @PathVariable Long id) throws IOException {
        AccountEntity accountEntity = AccountEntity.builder()
                .userId(id)
                .account(0L)
                .build();
        AccountDTO result = accountService.uploadAccount(accountEntity);
        return ResponseEntity.ok()
                .body(result);
    }

    @Operation(summary = "Пополнить аккаунт", description = "Пополняет аккаунт пользователя на указанную сумму.")
    @PostMapping("/put/money/{id}")
    public ResponseEntity<AccountDTO> putMoney(
            @Parameter(description = "ID пользователя") @PathVariable Long id,
            @Parameter(description = "Сумма пополнения") @RequestBody Long amount) {
        try {
            return ResponseEntity.ok()
                    .body(accountService.putMoney(id, amount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Списать деньги с аккаунта", description = "Списывает указанную сумму с аккаунта пользователя.")
    @PostMapping("/write/off/money/{id}")
    public ResponseEntity<AccountDTO> writeOffMoney(
            @Parameter(description = "ID пользователя") @PathVariable Long id,
            @Parameter(description = "Сумма для списания") @RequestBody Long amount) {
        try {
            return ResponseEntity.ok()
                    .body(accountService.writeOffMoney(id, amount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Получить все аккаунты", description = "Возвращает список всех аккаунтов.")
    @GetMapping("/all")
    public ResponseEntity<List<AccountDTO>> getAllAccount() {
        List<AccountDTO> ans = accountRepository.findAll().stream().map(
                (item) -> new AccountDTO(item.getUserId(), item.getAccount()))
                .toList();
        return ResponseEntity.ok()
                .body(ans);
    }
}
