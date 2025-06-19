package ru.hse.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.shop.dto.AccountDTO;
import ru.hse.shop.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/all")
    public ResponseEntity<List<AccountDTO>> getAllAccount() {
        List<AccountDTO> ans = paymentService.getAllAccount();
        return ResponseEntity.ok()
                .body(ans);
    }

    @PostMapping("/put/money/{id}")
    public ResponseEntity<AccountDTO> putMoney(@PathVariable Long id, @RequestBody Long amount) {
        AccountDTO ans = paymentService.putMoney(id, amount);
        return ResponseEntity.ok()
                .body(ans);
    }
}
