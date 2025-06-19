package org.example.repository;

import org.example.entity.InboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboxEventRepository extends JpaRepository<InboxEventEntity, String> {
} 