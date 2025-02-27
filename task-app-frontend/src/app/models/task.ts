export interface Task {
    id?: number;
    title: string;
    description?: string;
    dueDate: string | null;
    completed: boolean;
    userId?: number;
    notifyBefore?: number; // minutos antes para notificar
} 