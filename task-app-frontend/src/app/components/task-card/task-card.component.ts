import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { Task } from '../../models/task';

@Component({
  selector: 'app-task-card',
  templateUrl: './task-card.component.html',
  styleUrls: ['./task-card.component.css'],
  standalone: false
})
export class TaskCardComponent {
  @Input() task!: Task;
  @Output() taskCompleted = new EventEmitter<Task>();
  @Output() taskDeleted = new EventEmitter<void>();

  constructor(private router: Router) {}

  onCardClick(event: Event): void {
    this.router.navigate(['/tasks/edit', this.task.id]);
  }

  onCompletedChange(): void {
    this.taskCompleted.emit(this.task);
  }

  onDelete(event: Event): void {
    event.stopPropagation(); // Previne a navegação ao deletar
    this.taskDeleted.emit();
  }
} 