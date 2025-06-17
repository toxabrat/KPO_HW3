package ru.hse.shop.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.shop.dto.request.OrderCreateDTO;
import ru.hse.shop.dto.response.OrderDTO;
import ru.hse.shop.model.OrderStatus;
import ru.hse.shop.service.OrderService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    OrderService orderService;

    @PutMapping("/add")
    public ResponseEntity<OrderDTO> addOrder(@RequestBody OrderCreateDTO orderDTO) {
        OrderDTO ans = orderService.createOrder(orderDTO);
        return  ResponseEntity.ok()
                .body(ans);
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderDTO>> allOrder() {
        List<OrderDTO> ans = orderService.allOrders();
        return ResponseEntity.ok()
                .body(ans);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<OrderDTO> getStatus(@PathVariable Long id) {
        OrderDTO ans = orderService.getStatus(id);
        return ResponseEntity.ok()
                .body(ans);
    }

    @PostMapping("/status/{id}")
    public ResponseEntity<OrderDTO> setStatus(@PathVariable Long id, @RequestBody OrderStatus status) {
        OrderDTO ans = orderService.setStatus(id, status);
        return ResponseEntity.ok()
                .body(ans);
    }
}
