package com.example.financebackend.controller;

import com.example.financebackend.model.Record;
import com.example.financebackend.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Handles financial record operations with role-based access control

@RestController
@RequestMapping("/records")
public class RecordController {

    @Autowired
    private RecordService service;

    // CREATE (ADMIN only)
    @PostMapping
    public Record createRecord(@RequestBody Record record) {

        if (record.getAmount() <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        if (record.getType() == null) {
            throw new RuntimeException("Type is required");
        }

        if (!record.getUser().getRole().equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("Only ADMIN can create records");
        }

        return service.createRecord(record);
    }

    // GET records by user (ANALYST + ADMIN only)
    @GetMapping("/user/{userId}")
    public List<Record> getUserRecords(@PathVariable Long userId) {

        List<Record> records = service.getRecordsByUser(userId);

        if (records.isEmpty()) {
            return records;
        }

        String role = records.get(0).getUser().getRole();

        if (role.equalsIgnoreCase("VIEWER")) {
            throw new RuntimeException("VIEWER can only access dashboard");
        }

        return records;
    }

    // DASHBOARD (ALL roles allowed)

    @GetMapping("/summary/{userId}/income")
    public double userIncome(@PathVariable Long userId) {
        return service.getUserIncome(userId);
    }

    @GetMapping("/summary/{userId}/expense")
    public double userExpense(@PathVariable Long userId) {
        return service.getUserExpense(userId);
    }

    @GetMapping("/summary/{userId}/balance")
    public double userBalance(@PathVariable Long userId) {
        return service.getUserBalance(userId);
    }

    // DELETE (ADMIN only)
    @DeleteMapping("/{id}")
    public String deleteRecord(@PathVariable Long id) {

        Record record = service.getAllRecords()
                .stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Record not found"));

        if (!record.getUser().getRole().equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("Only ADMIN can delete records");
        }

        service.deleteRecord(id);
        return "Record deleted successfully";
    }

    // UPDATE (ADMIN only)
    @PutMapping("/{id}")
    public Record updateRecord(@PathVariable Long id, @RequestBody Record record) {

        if (!record.getUser().getRole().equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("Only ADMIN can update records");
        }

        return service.updateRecord(id, record);
    }

    @GetMapping("/summary/{userId}/category")
    public Map<String, Double> categoryTotals(@PathVariable Long userId) {
        return service.getCategoryTotals(userId);
    }

    @GetMapping("/summary/{userId}/recent")
    public List<Record> recentRecords(@PathVariable Long userId) {
        return service.getRecentRecords(userId);
    }

    @GetMapping("/summary/{userId}/monthly")
    public Map<String, Double> monthlyTotals(@PathVariable Long userId) {
        return service.getMonthlyTotals(userId);
    }

    @GetMapping("/filter/type")
    public List<Record> filterByType(@RequestParam Long userId,
                                     @RequestParam String type) {
        return service.filterByType(userId, type);
    }

    @GetMapping("/filter/category")
    public List<Record> filterByCategory(@RequestParam Long userId,
                                         @RequestParam String category) {
        return service.filterByCategory(userId, category);
    }

    @GetMapping("/filter/date")
    public List<Record> filterByDate(@RequestParam Long userId,
                                     @RequestParam String date) {
        return service.filterByDate(userId, date);
    }

}