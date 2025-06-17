package ru.hse.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.shop.entity.OrderEntity;

import java.util.ArrayList;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    ArrayList<OrderEntity> findBySenderId(Long senderId);
}
