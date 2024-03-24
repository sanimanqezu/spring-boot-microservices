import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {SidebarComponent} from "../../shared/sidebar/sidebar.component";
import {DataTableComponent} from "../../shared/data-table/data-table/data-table.component";
import {Subscription} from "rxjs";
import {OrderService} from "../../services/order-service/order.service";
import {Order} from "../../modules/order.module";
import {AlertComponent} from "../../shared/alert/alert.component";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SidebarComponent, DataTableComponent, AlertComponent, NgIf],
  templateUrl: './order.component.html',
  styleUrl: './order.component.css'
})

export class OrderComponent implements OnInit, OnDestroy{

  @ViewChild(DataTableComponent) dataTableComponent!: DataTableComponent;
  title: string = 'Order';
  headers: string[] = ['Order Number', 'Quantity', 'Products', 'Orderer Full Name', 'Orderer Id No'];
  myData: Order[] = [];
  orderSubscription: Subscription | undefined;
  dataTableObjectFields!: { label: string; value: string }[];
  dataTableShowModal: boolean = false;
  errorMessage: string = '';

  constructor(private orderService: OrderService) {}

  ngOnInit() {
    this.getAllOrders();
  }

  getAllOrders() {
    this.orderSubscription = this.orderService.getAllOrders()
      .subscribe(
        response => {
          this.myData = response;
          console.log("All orders: ", this.myData);
        },
        error => {
          console.error("Error retrieving orders:", error.error);
          this.errorMessage = "Failed to retrieve orders. Please try again later.";
        }
      );
  }

  handleSave(newObject: any) {
    this.orderService.addOrder(newObject)
      .subscribe(
        response => {
          console.log("Post response: ", response)
          console.log("All orders after post request: ", this.myData)
        },
        error => {
          console.error("Error adding order:", error.error);
          this.errorMessage = "Failed to add order. Please try again later.";
        }
      );
  }

  handleUpdate(selectedObject: any) {
    const objectId = selectedObject.id;
    this.orderService.updateOrder(objectId, selectedObject)
      .subscribe(
        response => {
          console.log('Order updated successfully:', response);
          this.getAllOrders();
        },
        error => {
          console.error('Error updating order:', error.error);
          this.errorMessage = "Failed to update order. Please try again later.";
        }
      );
  }

  handleDelete(selectedObject: any) {
    const objectId = selectedObject.id;
    this.orderService.deleteOrder(objectId)
      .subscribe(
        response => {
          console.log("Order deleted successfully: ", response)
          this.getAllOrders();
        },
        error => {
          console.error("Error deleting order:", error.error);
          this.errorMessage = "Failed to delete order. Please try again later.";
        }
      );
  }

  ngOnDestroy() {
    if (this.orderSubscription) {
      this.orderSubscription.unsubscribe();
    }
  }
}
