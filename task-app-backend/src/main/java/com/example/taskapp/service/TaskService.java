package com.example.taskapp.service;

import com.example.taskapp.dto.TaskDTO;
import com.example.taskapp.entity.Task;
import com.example.taskapp.entity.User;
import com.example.taskapp.repository.TaskRepository;
import com.example.taskapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
   private final TaskRepository taskRepository;
   private final UserRepository userRepository;

   public List<TaskDTO> getAllTasks() {
      User currentUser = getCurrentUser();
      return taskRepository.findByUserId(currentUser.getId())
               .stream()
               .map(task -> convertToDTO(task))
               .collect(Collectors.toList());
   }

   public TaskDTO getTask(Long id) {
      User currentUser = getCurrentUser();
      Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        
      if (!task.getUser().getId().equals(currentUser.getId())) {
         throw new RuntimeException("Access denied");
      }
        
      return convertToDTO(task);
   }

   public TaskDTO createTask(TaskDTO taskDTO) {
      User currentUser = getCurrentUser();
      Task task = new Task();
      task.setTitle(taskDTO.getTitle());
      task.setDescription(taskDTO.getDescription());
      task.setDueDate(taskDTO.getDueDate());
      task.setCompleted(taskDTO.isCompleted());
      task.setUser(currentUser);
        
      Task savedTask = taskRepository.save(task);
      return convertToDTO(savedTask);
   }

   public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
      User currentUser = getCurrentUser();
      Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        
      if (!task.getUser().getId().equals(currentUser.getId())) {
         throw new RuntimeException("Access denied");
      }
        
      task.setTitle(taskDTO.getTitle());
      task.setDescription(taskDTO.getDescription());
      task.setDueDate(taskDTO.getDueDate());
      task.setCompleted(taskDTO.isCompleted());
       
      Task updatedTask = taskRepository.save(task);
      return convertToDTO(updatedTask);
   }

   public void deleteTask(Long id) {
      User currentUser = getCurrentUser();
      Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        
      if (!task.getUser().getId().equals(currentUser.getId())) {
         throw new RuntimeException("Access denied");
      }
        
      taskRepository.delete(task);
   }

   private User getCurrentUser() {
      String username = SecurityContextHolder.getContext().getAuthentication().getName();
      return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
   }

   private TaskDTO convertToDTO(Task task) {
      return TaskDTO.builder()
               .id(task.getId())
               .title(task.getTitle())
               .description(task.getDescription())
               .dueDate(task.getDueDate())
               .completed(task.isCompleted())
               .build();
   }
} 