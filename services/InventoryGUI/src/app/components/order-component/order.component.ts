import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgIf } from '@angular/common';
import { Subscription } from 'rxjs';
import { DataTableComponent } from '../../shared/data-table/data-table/data-table.component';
import { AlertComponent } from '../../shared/alert/alert.component';
import { OrderService } from '../../services/order-service/order.service';
import { Order } from '../../modules/order.module';

@Component({
  selector: 'app-order',
  standalone: true,
  imports: [DataTableComponent, AlertComponent, NgIf],
  templateUrl: './order.component.html',
  styleUrl: './order.component.css'
})
export class OrderComponent implements OnInit, OnDestroy {
  title = 'Order';
  headers = ['Order Number', 'Quantity', 'Orderer Full Name', 'Orderer Id No', 'Order Items'];
  myData: Order[] = [];
  isLoading = false;

  alertMessage = '';
  alertType: 'success' | 'error' | 'warning' | 'info' = 'error';

  private sub?: Subscription;

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.isLoading = true;
    this.sub = this.orderService.getAllOrders().subscribe({
      next: (orders) => {
        this.myData = orders;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  handleSave(newObject: any): void {
    const idNo = newObject['ordererIdNo'] as string;
    if (!idNo || idNo.length !== 13) {
      this.showAlert('Orderer Id No must be exactly 13 digits.', 'error');
      return;
    }
    this.isLoading = true;
    this.orderService.addOrder(newObject as unknown as Order).subscribe({
      next: () => {
        this.showAlert('Order placed successfully.', 'success');
        this.loadOrders();
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  handleUpdate(selectedObject: any): void {
    const idNo = selectedObject['ordererIdNo'] as string;
    if (!idNo || idNo.length !== 13) {
      this.showAlert('Orderer Id No must be exactly 13 digits.', 'error');
      return;
    }
    const id = selectedObject['id'] as string;
    this.isLoading = true;
    this.orderService.updateOrder(id, selectedObject as unknown as Order).subscribe({
      next: () => {
        this.showAlert('Order updated successfully.', 'success');
        this.loadOrders();
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  handleDelete(selectedObject: any): void {
    const id = selectedObject['id'] as string;
    this.isLoading = true;
    this.orderService.deleteOrder(id).subscribe({
      next: () => {
        this.myData = this.myData.filter(o => o.id !== id);
        this.isLoading = false;
        this.showAlert('Order deleted successfully.', 'success');
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
