package com.example.financebackend.service;

import com.example.financebackend.model.Record;
import com.example.financebackend.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecordService {

    @Autowired
    private RecordRepository repo;

    public Record createRecord(Record record) {
        return repo.save(record);
    }

    public List<Record> getAllRecords() {
        return repo.findAll();
    }

    public List<Record> getRecordsByUser(Long userId) {
        return repo.findByUserId(userId);
    }

    public double getUserIncome(Long userId) {
        return repo.findByUserId(userId).stream()
                .filter(r -> r.getType().equalsIgnoreCase("INCOME"))
                .mapToDouble(Record::getAmount)
                .sum();
    }

    public double getUserExpense(Long userId) {
        return repo.findByUserId(userId).stream()
                .filter(r -> r.getType().equalsIgnoreCase("EXPENSE"))
                .mapToDouble(Record::getAmount)
                .sum();
    }

    public double getUserBalance(Long userId) {
        return getUserIncome(userId) - getUserExpense(userId);
    }

    public void deleteRecord(Long id) {
        repo.deleteById(id);
    }

    public Record updateRecord(Long id, Record updatedRecord) {
        Record record = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        record.setAmount(updatedRecord.getAmount());
        record.setType(updatedRecord.getType());
        record.setCategory(updatedRecord.getCategory());
        record.setDate(updatedRecord.getDate());
        record.setNotes(updatedRecord.getNotes());

        return repo.save(record);
    }

    public Map<String, Double> getCategoryTotals(Long userId) {
        List<Record> records = repo.findByUserId(userId);
        Map<String, Double> categoryTotals = new HashMap<>();

        for (Record r : records) {
            categoryTotals.put(
                    r.getCategory(),
                    categoryTotals.getOrDefault(r.getCategory(), 0.0) + r.getAmount()
            );
        }
        return categoryTotals;
    }

    public List<Record> getRecentRecords(Long userId) {
        return repo.findByUserId(userId)
                .stream()
                .sorted((r1, r2) -> r2.getId().compareTo(r1.getId()))
                .limit(5)
                .toList();
    }

    public Map<String, Double> getMonthlyTotals(Long userId) {
        List<Record> records = repo.findByUserId(userId);
        Map<String, Double> monthlyTotals = new HashMap<>();

        for (Record r : records) {
            String month = r.getDate().substring(0, 7); // YYYY-MM
            monthlyTotals.put(
                    month,
                    monthlyTotals.getOrDefault(month, 0.0) + r.getAmount()
            );
        }
        return monthlyTotals;
    }
    public List<Record> filterByType(Long userId, String type) {
        return repo.findByUserIdAndType(userId, type);
    }

    public List<Record> filterByCategory(Long userId, String category) {
        return repo.findByUserIdAndCategory(userId, category);
    }

    public List<Record> filterByDate(Long userId, String date) {
        return repo.findByUserIdAndDate(userId, date);
    }
}
