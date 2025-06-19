package ru.hse.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.shop.entity.OrderOutboxEntity;

import java.util.List;

public interface OrderOutboxRepository extends JpaRepository<OrderOutboxEntity, Long> {
    List<OrderOutboxEntity> findBySentFalse();
}