import { Component, OnInit } from '@angular/core';
import { TaskService } from '../../services/task.service';
import { Task } from '../../models/task';


@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css'],
  standalone: false
})
export class TaskListComponent implements OnInit {
  completedTasks: Task[] = [];
  inProgressTasks: Task[] = [];
  overdueTasks: Task[] = [];
  selectedTab = 1;

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    this.loadTasks();
  }

  loadTasks(): void {
    this.taskService.getTasks().subscribe(tasks => {
      const now = new Date();
      
      // Resetar arrays
      this.inProgressTasks = [];
      this.completedTasks = [];
      this.overdueTasks = [];

      tasks.forEach(task => {
        const dueDate = task.dueDate ? new Date(task.dueDate) : null;
        
        if (task.completed) {
          this.completedTasks.push(task);
        } else if (dueDate && dueDate < now) {
          this.overdueTasks.push(task);
        } else {
          this.inProgressTasks.push(task);
        }
      });

      // Ordenar por data
      this.inProgressTasks.sort((a, b) => this.compareDates(a.dueDate, b.dueDate));
      this.completedTasks.sort((a, b) => this.compareDates(a.dueDate, b.dueDate));
      this.overdueTasks.sort((a, b) => this.compareDates(a.dueDate, b.dueDate));
    });
  }

  private compareDates(dateA: string | null, dateB: string | null): number {
    if (!dateA && !dateB) return 0;
    if (!dateA) return 1;
    if (!dateB) return -1;
    return new Date(dateA).getTime() - new Date(dateB).getTime();
  }

  onTaskCompleted(task: Task): void {
    task.completed = !task.completed;
    this.taskService.updateTask(task.id!, task).subscribe(() => {
      this.loadTasks();
    });
  }

  onTaskDeleted(): void {
    this.loadTasks();
  }
} 