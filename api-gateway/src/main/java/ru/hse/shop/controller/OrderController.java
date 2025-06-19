package ru.hse.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.shop.dto.OrderCreateDTO;
import ru.hse.shop.dto.OrderDTO;
import ru.hse.shop.model.OrderStatus;
import ru.hse.shop.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<List<OrderDTO>> allOrder() {
        List<OrderDTO> ans = orderService.allOrders();
        return ResponseEntity.ok()
                .body(ans);
    }

    @PostMapping("/status/{id}")
    public ResponseEntity<OrderDTO> setStatus(@PathVariable Long id, @RequestBody OrderStatus status) {
        OrderDTO ans = orderService.setStatus(id, status);
        return ResponseEntity.ok()
                .body(ans);
    }

    @PutMapping("/add")
    public ResponseEntity<OrderDTO> addOrder(@RequestBody OrderCreateDTO orderDTO) {
        OrderDTO ans = orderService.createOrder(orderDTO);
        return ResponseEntity.ok()
                .body(ans);
    }
}
