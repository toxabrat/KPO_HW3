package ru.hse.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.shop.dto.AccountDTO;
import ru.hse.shop.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Operation(summary = "Получить все аккаунты", description = "Возвращает список всех аккаунтов.")
    @GetMapping("/all")
    public ResponseEntity<List<AccountDTO>> getAllAccount() {
        List<AccountDTO> ans = paymentService.getAllAccount();
        return ResponseEntity.ok()
                .body(ans);
    }

    @Operation(summary = "Пополнить аккаунт", description = "Пополняет аккаунт пользователя на указанную сумму.")
    @PostMapping("/put/money/{id}")
    public ResponseEntity<AccountDTO> putMoney(
            @Parameter(description = "ID пользователя") @PathVariable Long id,
            @Parameter(description = "Сумма пополнения") @RequestBody Long amount) {
        AccountDTO ans = paymentService.putMoney(id, amount);
        return ResponseEntity.ok()
                .body(ans);
    }

    @Operation(summary = "Создать новый аккаунт", description = "Создаёт новый аккаунт с балансом 0 для пользователя по id.")
    @PutMapping("/new/{id}")
    public ResponseEntity<AccountDTO> newAccount(
            @Parameter(description = "ID пользователя") @PathVariable Long id) {
        AccountDTO ans = paymentService.newAccount(id);
        return ResponseEntity.ok().body(ans);
    }
}
