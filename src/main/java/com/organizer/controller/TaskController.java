package com.organizer.controller;

import com.organizer.entity.Task;
import com.organizer.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<Task> getAll() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public Task getById(@PathVariable Long id) {
        return taskService.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @GetMapping("/wedding/{weddingId}")
    public List<Task> getByWeddingId(@PathVariable Long weddingId) {
        return taskService.findByWeddingId(weddingId);
    }

    @GetMapping("/member/{memberId}")
    public List<Task> getByAssignedToId(@PathVariable Long memberId) {
        return taskService.findByAssignedToId(memberId);
    }

    @GetMapping("/status/{status}")
    public List<Task> getByStatus(@PathVariable String status) {
        return taskService.findByStatus(status);
    }

    @GetMapping("/overdue")
    public List<Task> getOverdueTasks() {
        return taskService.findOverdueTasks(LocalDate.now());
    }

    @PostMapping
    public Task create(@RequestBody Task task) {
        return taskService.save(task);
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @RequestBody Task task) {
        return taskService.update(id, task);
    }

    @PatchMapping("/{id}/done")
    public Task markAsDone(@PathVariable Long id) {
        return taskService.markAsDone(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}