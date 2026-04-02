package com.example.financebackend.repository;

import com.example.financebackend.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {

    List<Record> findByUserId(Long userId);
    List<Record> findByUserIdAndType(Long userId, String type);
    List<Record> findByUserIdAndCategory(Long userId, String category);
    List<Record> findByUserIdAndDate(Long userId, String date);
}