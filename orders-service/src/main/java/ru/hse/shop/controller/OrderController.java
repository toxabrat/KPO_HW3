package ru.hse.shop.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.shop.dto.request.OrderCreateDTO;
import ru.hse.shop.dto.response.OrderDTO;
import ru.hse.shop.model.OrderStatus;
import ru.hse.shop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    OrderService orderService;

    @Operation(summary = "Создать заказ", description = "Создаёт новый заказ. В теле запроса указываются senderId, receiverId, transactionAmount.")
    @PutMapping("/add")
    public ResponseEntity<OrderDTO> addOrder(@RequestBody OrderCreateDTO orderDTO) {
        OrderDTO ans = orderService.createOrder(orderDTO);
        return ResponseEntity.ok()
                .body(ans);
    }

    @Operation(summary = "Получить все заказы", description = "Возвращает список всех заказов.")
    @GetMapping("/all")
    public ResponseEntity<List<OrderDTO>> allOrder() {
        List<OrderDTO> ans = orderService.allOrders();
        return ResponseEntity.ok()
                .body(ans);
    }

    @Operation(summary = "Получить статус заказа", description = "Возвращает статус заказа по id.")
    @GetMapping("/status/{id}")
    public ResponseEntity<OrderDTO> getStatus(
            @Parameter(description = "ID заказа") @PathVariable Long id) {
        OrderDTO ans = orderService.getStatus(id);
        return ResponseEntity.ok()
                .body(ans);
    }

    @Operation(summary = "Установить статус заказа", description = "Устанавливает статус заказа по id. Возможные значения статуса: CREATED, COMPLETED, FAILED.")
    @PostMapping("/status/{id}")
    public ResponseEntity<OrderDTO> setStatus(
            @Parameter(description = "ID заказа") @PathVariable Long id,
            @Parameter(description = "Статус заказа (CREATED, COMPLETED, FAILED)") @RequestBody OrderStatus status) {
        OrderDTO ans = orderService.setStatus(id, status);
        return ResponseEntity.ok()
                .body(ans);
    }
}
