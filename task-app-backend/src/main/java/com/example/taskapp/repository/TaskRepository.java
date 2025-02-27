package com.example.taskapp.repository;

import com.example.taskapp.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
    List<Task> findByUserIdAndCompleted(Long userId, boolean completed);
    List<Task> findByUserIdAndDueDateBefore(Long userId, LocalDateTime date);
    List<Task> findByUserIdOrderByDueDateAsc(Long userId);
}
