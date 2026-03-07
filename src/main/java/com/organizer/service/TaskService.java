package com.organizer.service;

import com.organizer.entity.Task;
import com.organizer.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> findByWeddingId(Long weddingId) {
        return taskRepository.findByWeddingId(weddingId);
    }

    public List<Task> findByAssignedToId(Long memberId) {
        return taskRepository.findByAssignedToId(memberId);
    }

    public List<Task> findByStatus(String status) {
        return taskRepository.findByStatus(status);
    }

    public List<Task> findOverdueTasks(LocalDate date) {
        return taskRepository.findByDueDateBeforeAndStatusNot(date, "DONE");
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public Task update(Long id, Task updatedTask) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setDueDate(updatedTask.getDueDate());
        task.setStatus(updatedTask.getStatus());
        task.setWedding(updatedTask.getWedding());
        task.setAssignedTo(updatedTask.getAssignedTo());

        return taskRepository.save(task);
    }

    public Task markAsDone(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus("DONE");

        return taskRepository.save(task);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}