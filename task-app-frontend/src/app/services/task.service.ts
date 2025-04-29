import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, catchError, throwError } from 'rxjs';

@Injectable({
   providedIn: 'root'
})
export class TaskService {
   private apiUrl = 'http://localhost:8080/api/tasks';

   constructor(private http: HttpClient) { }

   getTasks(): Observable<any[]> {
      return this.http.get<any[]>(this.apiUrl);
   }

   getTask(id: number): Observable<any> {
      return this.http.get<any>(`${this.apiUrl}/${id}`);
   }

   createTask(task: any): Observable<any> {
      console.log('Creating task with data:', task);
      // Para criar uma nova task, usamos apenas o URL base
      return this.http.post<any>(this.apiUrl, task).pipe(
         tap(response => console.log('Create task response:', response)),
         catchError(error => {
            console.error('Error creating task:', error);
            return throwError(() => error);
         })
      );
   }

   updateTask(id: number, task: any): Observable<any> {
      return this.http.put<any>(`${this.apiUrl}/${id}`, task);
   }

   deleteTask(id: number): Observable<void> {
      return this.http.delete<void>(`${this.apiUrl}/${id}`);
   }
} 