import { Component, OnDestroy, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgIf } from '@angular/common';
import { Subscription, forkJoin } from 'rxjs';
import { UserService } from '../../services/user-service/user.service';
import { ProductService } from '../../services/product-service/product.service';
import { OrderService } from '../../services/order-service/order.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink, NgIf],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit, OnDestroy {
  userCount = 0;
  productCount = 0;
  orderCount = 0;
  isLoading = true;
  loadError = false;

  private sub?: Subscription;

  constructor(
    private userService: UserService,
    private productService: ProductService,
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
    this.loadStats();
  }

  loadStats(): void {
    this.isLoading = true;
    this.loadError = false;

    this.sub = forkJoin({
      users: this.userService.getAllUsers(),
      products: this.productService.getAllProducts(),
      orders: this.orderService.getAllOrders()
    }).subscribe({
      next: ({ users, products, orders }) => {
        this.userCount = users.length;
        this.productCount = products.length;
        this.orderCount = orders.length;
        this.isLoading = false;
      },
      error: () => {
        this.loadError = true;
        this.isLoading = false;
      }
    });
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }
}
