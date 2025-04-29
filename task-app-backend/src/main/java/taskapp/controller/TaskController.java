package taskapp.controller;

import taskapp.dto.TaskDTO;
import taskapp.service.TaskService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD operations for tasks
 */
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
   private final TaskService taskService;

   @GetMapping
   public ResponseEntity<List<TaskDTO>> getAllTasks() {
      return ResponseEntity.ok(taskService.getAllTasks());
   }

   @GetMapping("/{id}")
   public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
      return ResponseEntity.ok(taskService.getTask(id));
   }

   @PostMapping
   public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
      return ResponseEntity.ok(taskService.createTask(taskDTO));
   }

   @PutMapping("/{id}")
   public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
      return ResponseEntity.ok(taskService.updateTask(id, taskDTO));
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
      taskService.deleteTask(id);
      return ResponseEntity.ok().build();
   }
} 