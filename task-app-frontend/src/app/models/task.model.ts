export interface Task {
   id?: number;
   title: string;
   description?: string;
   dueDate?: Date;
   completed: boolean;
}

export interface AuthRequest {
   username: string;
   password: string;
}

export interface AuthResponse {
   token: string;
}
