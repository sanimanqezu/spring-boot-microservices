import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {SidebarComponent} from "../../shared/sidebar/sidebar.component";
import {DataTableComponent} from "../../shared/data-table/data-table/data-table.component";
import {Subscription} from "rxjs";
import {Product} from "../../modules/product.module";
import {ProductService} from "../../services/product-service/product.service";
import {AlertComponent} from "../../shared/alert/alert.component";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SidebarComponent, DataTableComponent, AlertComponent, NgIf],
  templateUrl: './product.component.html',
  styleUrl: './product.component.css'
})

export class ProductComponent implements OnInit, OnDestroy {

  @ViewChild(DataTableComponent) dataTableComponent!: DataTableComponent;
  title: string = 'Product';
  headers: string[] = ['Product Name', 'Product Number', 'Quantity', 'Expiration Date'];
  myData: Product[] = [];
  productSubscription: Subscription | undefined;
  dataTableObjectFields!: { label: string; value: string }[];
  dataTableShowModal: boolean = false;
  errorMessage: string = '';

  constructor(private productService: ProductService) {}

  ngOnInit() {
    this.getAllProducts();
  }

  getAllProducts() {
    this.productSubscription = this.productService.getAllProducts()
      .subscribe(
        response => {
          this.myData = response;
          console.log("All products: ", this.myData);
        },
        error => {
          console.error("Error retrieving products:", error.error);
          this.errorMessage = "Failed to retrieve products. Please try again later.";
        }
      );
  }

  handleSave(newObject: any) {
    this.productService.addProduct(newObject)
      .subscribe(
        response => {
          console.log("Post response: ", response)
          console.log("All products after post request: ", this.myData)
        },
        error => {
          console.error("Error adding product:", error.error);
          this.errorMessage = "Failed to add product. Please try again later.";
        }
      );
  }

  handleUpdate(selectedObject: any) {
    const objectId = selectedObject.id;
    this.productService.updateProduct(objectId, selectedObject)
      .subscribe(
        response => {
          console.log('Product updated successfully:', response);
          this.getAllProducts();
        },
        error => {
          console.error('Error updating product:', error.error);
          this.errorMessage = "Failed to update product. Please try again later.";
        }
      );
  }

  handleDelete(selectedObject: any) {
    const objectId = selectedObject.id;
    this.productService.deleteProduct(objectId)
      .subscribe(
        response => {
          console.log("Product deleted successfully: ", response)
          this.getAllProducts();
        },
        error => {
          console.error("Error deleting product:", error.error);
          this.errorMessage = "Failed to delete product. Please try again later.";
        }
      );
  }

  ngOnDestroy() {
    if (this.productSubscription) {
      this.productSubscription.unsubscribe();
    }
  }
}
