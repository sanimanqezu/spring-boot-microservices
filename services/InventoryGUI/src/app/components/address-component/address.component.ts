import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgIf } from '@angular/common';
import { Subscription } from 'rxjs';
import { DataTableComponent } from '../../shared/data-table/data-table/data-table.component';
import { AlertComponent } from '../../shared/alert/alert.component';
import { AddressService } from '../../services/address-service/address.service';
import { Address } from '../../modules/address.module';

@Component({
  selector: 'app-address',
  standalone: true,
  imports: [DataTableComponent, AlertComponent, NgIf],
  templateUrl: './address.component.html',
  styleUrl: './address.component.css'
})
export class AddressComponent implements OnInit, OnDestroy {
  title = 'Address';
  headers = ['House Number', 'Street Name', 'City', 'Zip Code'];
  myData: Address[] = [];
  isLoading = false;

  alertMessage = '';
  alertType: 'success' | 'error' | 'warning' | 'info' = 'error';

  private sub?: Subscription;

  constructor(private addressService: AddressService) {}

  ngOnInit(): void {
    this.loadAddresses();
  }

  loadAddresses(): void {
    this.isLoading = true;
    this.sub = this.addressService.getAllAddresses().subscribe({
      next: (addresses) => {
        this.myData = addresses;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  handleSave(newObject: any): void {
    this.isLoading = true;
    this.addressService.addAddress(newObject as unknown as Address).subscribe({
      next: () => {
        this.showAlert('Address added successfully.', 'success');
        this.loadAddresses();
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  handleUpdate(selectedObject: any): void {
    const id = selectedObject['id'] as string;
    this.isLoading = true;
    this.addressService.updateAddress(id, selectedObject as unknown as Address).subscribe({
      next: () => {
        this.showAlert('Address updated successfully.', 'success');
        this.loadAddresses();
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  handleDelete(selectedObject: any): void {
    const id = selectedObject['id'] as string;
    this.isLoading = true;
    this.addressService.deleteAddress(id).subscribe({
      next: () => {
        this.myData = this.myData.filter(a => a.id !== id);
        this.isLoading = false;
        this.showAlert('Address deleted successfully.', 'success');
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
