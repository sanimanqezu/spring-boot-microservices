import { Component, OnDestroy, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Subscription } from 'rxjs';
import { SidebarComponent } from './shared/sidebar/sidebar.component';
import { FooterComponent } from './shared/footer/footer.component';
import { AlertComponent } from './shared/alert/alert.component';
import { NgIf } from '@angular/common';
import { NotificationService, Notification } from './services/notification-service/notification.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SidebarComponent, FooterComponent, AlertComponent, NgIf],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'Manqezu Inventory';
  globalMessage: string = '';
  globalMessageType: 'success' | 'error' | 'warning' | 'info' = 'error';

  private notificationSub!: Subscription;

  constructor(private notificationService: NotificationService) {}

  ngOnInit(): void {
    this.notificationSub = this.notificationService.notification$.subscribe(
      (notification: Notification) => {
        this.globalMessage = notification.message;
        this.globalMessageType = notification.type;
      }
    );
  }

  ngOnDestroy(): void {
    this.notificationSub?.unsubscribe();
  }
}
