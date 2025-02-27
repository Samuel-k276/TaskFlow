import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TaskService } from '../../services/task.service';
import { Task } from '../../models/task';

@Component({
  selector: 'app-task-form',
  templateUrl: './task-form.component.html',
  styleUrls: ['./task-form.component.css'],
  standalone: false
})
export class TaskFormComponent implements OnInit {
  taskForm: FormGroup;
  isEditMode = false;
  taskId?: number;
  minDate: Date = new Date();
  currentTime: string;

  constructor(
    private fb: FormBuilder,
    private taskService: TaskService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    // Configura a data mínima como início do dia atual
    this.minDate.setHours(0, 0, 0, 0);

    // Configura a hora atual
    const now = new Date();
    this.currentTime = `${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`;

    this.taskForm = this.fb.group({
      title: ['', Validators.required],
      description: [''],
      dueDate: [null, [this.dateValidator()]],
      dueTime: ['', [this.timeValidator()]],
      completed: [false],
      enableNotification: [false],
      notifyBefore: [{value: 15, disabled: true}]
    }); 

    // Observa mudanças na data para atualizar validação de hora
    this.taskForm.get('dueDate')?.valueChanges.subscribe(() => {
      this.taskForm.get('dueTime')?.updateValueAndValidity();
    });
  }

  ngOnInit(): void {
    this.taskId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.taskId) {
      this.isEditMode = true;
      this.taskService.getTask(this.taskId).subscribe(task => {
        const date = task.dueDate ? new Date(task.dueDate) : null;
        this.taskForm.patchValue({
          ...task,
          dueDate: date,
          dueTime: date ? this.formatTime(date) : ''
        });
      });
    }

    // Observar mudanças na data e hora
    this.taskForm.get('dueDate')?.valueChanges.subscribe(() => {
      this.taskForm.get('dueTime')?.updateValueAndValidity();
      this.updateNotificationVisibility();
    });

    this.taskForm.get('dueTime')?.valueChanges.subscribe(() => {
      this.updateNotificationVisibility();
    });
  }

  onNotificationChange(event: any): void {
    const notifyBeforeControl = this.taskForm.get('notifyBefore');
    if (event.checked) {
      notifyBeforeControl?.enable();
      this.requestNotificationPermission();
    } else {
      notifyBeforeControl?.disable();
    }
  }

  private requestNotificationPermission(): void {
    if ('Notification' in window) {
      Notification.requestPermission();
    }
  }

  onSubmit(): void {
    if (this.taskForm.valid) {
      const formValue = this.taskForm.value;
      const dueDate = this.combineDateAndTime(formValue.dueDate, formValue.dueTime);
      
      const task = {
        ...formValue,
        dueDate: dueDate,
        notifyBefore: formValue.enableNotification ? formValue.notifyBefore : null
      };
      
      delete task.dueTime;
      delete task.enableNotification;

      if (this.isEditMode && this.taskId) {
        this.taskService.updateTask(this.taskId, task).subscribe(() => {
          this.scheduleNotification(task);
          this.router.navigate(['/tasks']);
        });
      } else {
        this.taskService.createTask(task).subscribe(createdTask => {
          this.scheduleNotification(createdTask);
          this.router.navigate(['/tasks']);
        });
      }
    }
  }

  private scheduleNotification(task: Task): void {
    if (task.notifyBefore && task.dueDate) {
      const dueDate = new Date(task.dueDate);
      const notifyDate = new Date(dueDate.getTime() - (task.notifyBefore * 60000));
      
      const now = new Date();
      const timeUntilNotification = notifyDate.getTime() - now.getTime();

      if (timeUntilNotification > 0) {
        setTimeout(() => {
          this.showNotification(task);
        }, timeUntilNotification);
      }
    }
  }

  private showNotification(task: Task): void {
    if ('Notification' in window && Notification.permission === 'granted') {
      new Notification('Task Reminder', {
        body: `Task "${task.title}" is due in ${task.notifyBefore} minutes`,
        icon: '/assets/notification-icon.png' // Adicione um ícone apropriado
      });
    }
  }

  private formatTime(date: Date): string {
    return date.toTimeString().substring(0, 5);
  }

  private combineDateAndTime(date: Date | null, time: string): Date | null {
    if (!date) return null;
    
    if (time) {
      const [hours, minutes] = time.split(':').map(Number);
      date.setHours(hours, minutes);
    }
    
    return date;
  }

  private dateValidator() {
    return (control: any) => {
      const selectedDate = control.value;
      if (!selectedDate) {
        return null;
      }

      const today = new Date();
      today.setHours(0, 0, 0, 0);
      
      if (selectedDate < today) {
        return { pastDate: true };
      }
      return null;
    };
  }

  private timeValidator() {
    return (control: any) => {
      const time = control.value;
      const date = this.taskForm?.get('dueDate')?.value;
      
      if (!time || !date) {
        return null;
      }

      const now = new Date();
      const selectedDate = new Date(date);
      const [hours, minutes] = time.split(':').map(Number);
      
      // Se a data selecionada for hoje, verifica se a hora é futura
      if (selectedDate.toDateString() === now.toDateString()) {
        const currentHours = now.getHours();
        const currentMinutes = now.getMinutes();
        
        if (hours < currentHours || (hours === currentHours && minutes <= currentMinutes)) {
          return { pastTime: true };
        }
      }
      
      return null;
    };
  }

  private updateNotificationVisibility(): void {
    const hasDate = this.taskForm.get('dueDate')?.value;
    const hasTime = this.taskForm.get('dueTime')?.value;

    if (!hasDate || !hasTime) {
      this.taskForm.patchValue({
        enableNotification: false
      });
      this.taskForm.get('notifyBefore')?.disable();
    }
  }
} 