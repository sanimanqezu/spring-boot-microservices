import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { NotificationService } from '../notification-service/notification.service';

@Injectable({ providedIn: 'root' })
export class HttpInterceptorService implements HttpInterceptor {

  constructor(private notificationService: NotificationService) {}

  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        const message = this.resolveMessage(error);
        this.notificationService.showError(message);
        return throwError(() => error);
      })
    );
  }

  private resolveMessage(error: HttpErrorResponse): string {
    if (error.status === 0) {
      return 'Cannot connect to server. Ensure the backend is running.';
    }
    if (error.error?.message) {
      return error.error.message;
    }
    switch (error.status) {
      case 400: return 'Invalid request (400). Check the submitted data.';
      case 404: return 'Resource not found (404).';
      case 409: return 'Conflict — this record may already exist (409).';
      case 500: return 'Internal server error (500). Please try again.';
      default:  return `Request failed with status ${error.status}.`;
    }
  }
}
