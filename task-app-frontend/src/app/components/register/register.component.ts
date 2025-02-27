import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  standalone: false
})
export class RegisterComponent {
  registerForm: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(4)]]
    });
  }

  onSubmit(): void {  
    if (this.registerForm.valid) {
      this.errorMessage = '';
      this.authService.register(this.registerForm.value).subscribe({
        next: () => {
          this.router.navigate(['/login']);
        },
        error: (error: Error) => {
          console.log('Error in component:', error.message);
          switch (error.message) {
            case 'USERNAME_EXISTS':
              this.errorMessage = 'This username is already taken';
              break;
            case 'EMAIL_EXISTS':
              this.errorMessage = 'This email is already registered';
              break;
            default:
              this.errorMessage = 'Registration failed. Please try again.';
          }
        }
      });
    }
  }
} 