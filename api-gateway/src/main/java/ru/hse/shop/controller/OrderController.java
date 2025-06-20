package ru.hse.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.shop.dto.OrderCreateDTO;
import ru.hse.shop.dto.OrderDTO;
import ru.hse.shop.model.OrderStatus;
import ru.hse.shop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Operation(summary = "Получить все заказы", description = "Возвращает список всех заказов.")
    @GetMapping("/all")
    public ResponseEntity<List<OrderDTO>> allOrder() {
        List<OrderDTO> ans = orderService.allOrders();
        return ResponseEntity.ok()
                .body(ans);
    }

    @Operation(summary = "Создать заказ", description = "Создаёт новый заказ. В теле запроса указываются senderId, receiverId, transactionAmount.")
    @PutMapping("/add")
    public ResponseEntity<OrderDTO> addOrder(@RequestBody OrderCreateDTO orderDTO) {
        OrderDTO ans = orderService.createOrder(orderDTO);
        return ResponseEntity.ok()
                .body(ans);
    }
}
