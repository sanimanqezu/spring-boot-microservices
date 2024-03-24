import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {SidebarComponent} from "../../shared/sidebar/sidebar.component";
import {DataTableComponent} from "../../shared/data-table/data-table/data-table.component";
import {AddressService} from "../../services/address-service/address.service";
import {Subscription} from "rxjs";
import {Address} from "../../modules/address.module";
import {AlertComponent} from "../../shared/alert/alert.component";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SidebarComponent, DataTableComponent, AlertComponent, NgIf],
  templateUrl: './address.component.html',
  styleUrl: './address.component.css',
  providers: [AddressService]
})

export class AddressComponent implements OnInit, OnDestroy {

  @ViewChild(DataTableComponent) dataTableComponent!: DataTableComponent;
  title: string = 'Address';
  headers: string[] = ['House Number', 'Street Name', 'City', 'Zip Code'];
  myData: Address[] = [];
  addressSubscription: Subscription | undefined;
  dataTableObjectFields!: { label: string; value: string }[];
  dataTableShowModal: boolean = false;
  errorMessage: string = '';

  constructor(private addressService: AddressService) {}

  ngOnInit() {
    this.getAllAddresses();
  }

  getAllAddresses() {
    this.addressSubscription = this.addressService.getAllAddresses()
      .subscribe(
        response => {
          this.myData = response;
          console.log("All addresses: ", this.myData);
        },
        error => {
          console.error("Error retrieving addresses:", error.error);
          this.errorMessage = "Failed to retrieve addresses. Please try again later.";
        }
      );
  }

  handleSave(newObject: any) {
    this.addressService.addAddress(newObject)
      .subscribe(
        response => {
          console.log("Post response: ", response)
          console.log("All addresses after post request: ", this.myData)
        },
        error => {
          console.error("Error adding address:", error.error);
          this.errorMessage = "Failed to add address. Please try again later.";
        }
      );
  }

  handleUpdate(selectedObject: any) {
    const objectId = selectedObject.id;
    this.addressService.updateAddress(objectId, selectedObject)
      .subscribe(
        response => {
          console.log('Address updated successfully:', response);
          this.getAllAddresses();
        },
        error => {
          console.error('Error updating address:', error.error);
          this.errorMessage = "Failed to update address. Please try again later.";
        }
      );
  }

  handleDelete(selectedObject: any) {
    const objectId = selectedObject.id;
    this.addressService.deleteAddress(objectId)
      .subscribe(
        response => {
          console.log("Address deleted successfully: ", response)
          this.getAllAddresses();
        },
        error => {
          console.error("Error deleting address:", error.error);
          this.errorMessage = "Failed to delete address. Please try again later.";
        }
      );
  }

  ngOnDestroy() {
    if (this.addressSubscription) {
      this.addressSubscription.unsubscribe();
    }
  }
}
