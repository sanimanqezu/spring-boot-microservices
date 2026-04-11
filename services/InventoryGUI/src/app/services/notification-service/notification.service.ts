import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

export interface Notification {
  message: string;
  type: 'success' | 'error' | 'warning' | 'info';
}

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private subject = new Subject<Notification>();
  notification$ = this.subject.asObservable();

  showSuccess(message: string): void {
    this.subject.next({ message, type: 'success' });
  }

  showError(message: string): void {
    this.subject.next({ message, type: 'error' });
  }

  showWarning(message: string): void {
    this.subject.next({ message, type: 'warning' });
  }
}
