import {Component, OnDestroy, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {UserService} from "../../services/user-service/user.service";
import {ProductService} from "../../services/product-service/product.service";
import {OrderService} from "../../services/order-service/order.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit, OnDestroy {
  users: number;
  products: number;
  orders: number;
  usersSubscription!: Subscription;
  productsSubscription!: Subscription;
  ordersSubscription!: Subscription;

  constructor(private userService: UserService,
              private productService: ProductService,
              private orderService: OrderService) {
    this.users = 0;
    this.products = 0;
    this.orders = 0;
  }

  ngOnInit() {
    this.getAllOrders();
    this.getAllProducts();
    this.getAllUsers();
  }

  ngOnDestroy() {
    if (this.ordersSubscription != null) {
      this.ordersSubscription.unsubscribe();
    }

    if (this.productsSubscription != null) {
      this.productsSubscription.unsubscribe();
    }

    if (this.usersSubscription != null) {
      this.usersSubscription.unsubscribe();
    }
  }

  getAllUsers() {
    this.usersSubscription = this.userService.getAllUsers()
      .subscribe(resp => {
        this.users = resp.length;
      })
  }

  getAllProducts() {
    this.productsSubscription = this.productService.getAllProducts()
      .subscribe(resp => {
        this.products = resp.length;
      })
  }

  getAllOrders() {
    this.ordersSubscription = this.orderService.getAllOrders()
      .subscribe(resp => {
        this.orders = resp.length;
      })
  }
}
