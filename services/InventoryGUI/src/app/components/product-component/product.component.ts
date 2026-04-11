import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgIf } from '@angular/common';
import { Subscription } from 'rxjs';
import { DataTableComponent } from '../../shared/data-table/data-table/data-table.component';
import { AlertComponent } from '../../shared/alert/alert.component';
import { ProductService } from '../../services/product-service/product.service';
import { Product } from '../../modules/product.module';

@Component({
  selector: 'app-product',
  standalone: true,
  imports: [DataTableComponent, AlertComponent, NgIf],
  templateUrl: './product.component.html',
  styleUrl: './product.component.css'
})
export class ProductComponent implements OnInit, OnDestroy {
  title = 'Product';
  headers = ['Product Name', 'Product Number', 'Quantity', 'Expiration Date'];
  myData: Product[] = [];
  isLoading = false;

  alertMessage = '';
  alertType: 'success' | 'error' | 'warning' | 'info' = 'error';

  private sub?: Subscription;

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.isLoading = true;
    this.sub = this.productService.getAllProducts().subscribe({
      next: (products) => {
        this.myData = products.map(p => ({
          ...p,
          expirationDate: this.formatDate(p.expirationDate as unknown as number[])
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
      const [year, month, day, hours = 0, minutes = 0, seconds = 0] = date;
      return new Date(year, month - 1, day, hours, minutes, seconds).toLocaleString();
    }
    return String(date);
  }

  handleSave(newObject: any): void {
    this.isLoading = true;
    this.productService.addProduct(newObject as unknown as Product).subscribe({
      next: () => {
        this.showAlert('Product added successfully.', 'success');
        this.loadProducts();
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  handleUpdate(selectedObject: any): void {
    const id = selectedObject['id'] as string;
    this.isLoading = true;
    this.productService.updateProduct(id, selectedObject as unknown as Product).subscribe({
      next: () => {
        this.showAlert('Product updated successfully.', 'success');
        this.loadProducts();
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  handleDelete(selectedObject: any): void {
    const id = selectedObject['id'] as string;
    this.isLoading = true;
    this.productService.deleteProduct(id).subscribe({
      next: () => {
        this.myData = this.myData.filter(p => p.id !== id);
        this.isLoading = false;
        this.showAlert('Product deleted successfully.', 'success');
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
