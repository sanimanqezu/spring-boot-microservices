import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgIf } from '@angular/common';
import { Subscription } from 'rxjs';
import { DataTableComponent } from '../../shared/data-table/data-table/data-table.component';
import { AlertComponent } from '../../shared/alert/alert.component';
import { UserService } from '../../services/user-service/user.service';
import { User } from '../../modules/user.module';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [DataTableComponent, AlertComponent, NgIf],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css'
})
export class UserComponent implements OnInit, OnDestroy {
  title = 'User';
  headers = ['First Name', 'Last Name', 'Rsa Id', 'Date Of Birth', 'Address'];
  myData: User[] = [];
  isLoading = false;

  alertMessage = '';
  alertType: 'success' | 'error' | 'warning' | 'info' = 'error';

  private sub?: Subscription;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.isLoading = true;
    this.sub = this.userService.getAllUsers().subscribe({
      next: (users) => {
        this.myData = users.map(u => ({
          ...u,
          dateOfBirth: this.formatDate(u.dateOfBirth as unknown as number[])
        }));
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  private formatDate(date: number[] | string): string {
    if (Array.isArray(date)) {
      const [year, month, day] = date;
      return new Date(year, month - 1, day).toLocaleDateString();
    }
    return String(date);
  }

  handleSave(newObject: any): void {
    this.isLoading = true;
    this.userService.addUser(newObject as unknown as User).subscribe({
      next: () => {
        this.showAlert('User added successfully.', 'success');
        this.loadUsers();
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  handleUpdate(selectedObject: any): void {
    const id = selectedObject['id'] as string;
    this.isLoading = true;
    this.userService.updateUser(id, selectedObject as unknown as User).subscribe({
      next: () => {
        this.showAlert('User updated successfully.', 'success');
        this.loadUsers();
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  handleDelete(selectedObject: any): void {
    const id = selectedObject['id'] as string;
    this.isLoading = true;
    this.userService.deleteUser(id).subscribe({
      next: () => {
        this.myData = this.myData.filter(u => u.id !== id);
        this.isLoading = false;
        this.showAlert('User deleted successfully.', 'success');
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  private showAlert(message: string, type: 'success' | 'error'): void {
    this.alertMessage = message;
    this.alertType = type;
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }
}
