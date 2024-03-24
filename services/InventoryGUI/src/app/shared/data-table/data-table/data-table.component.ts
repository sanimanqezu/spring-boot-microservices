import {Component, EventEmitter, Input, Output} from '@angular/core';
import {DatePipe, formatDate, NgForOf, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {OrderItem} from "../../../modules/OrderItem.module";
import {Address} from "../../../modules/address.module";

@Component({
  selector: 'app-data-table',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    FormsModule,
    DatePipe
  ],
  templateUrl: './data-table.component.html',
  styleUrl: './data-table.component.css'
})
export class DataTableComponent {
  @Input() data: any;
  @Input() title!: string;
  @Input() showModal: boolean = false;
  @Input() objectFields: { label: string, value: any }[] = [];
  @Input() headers: string[] = [];

  @Output() saveObjectEvent: EventEmitter<void> = new EventEmitter<void>();
  @Output() updateObjectEvent: EventEmitter<void> = new EventEmitter<void>();
  @Output() deleteObjectEvent: EventEmitter<void> = new EventEmitter<void>();

  selectedItemId: any;
  showConfirmationModal: any;
  modalOpened!: boolean;
  isUpdating!: boolean;
  orderItems: OrderItem[] = [];
  userAddress: Address = { id: '', city: '', streetName: '', houseNumber: '', zipCode: '' };

  toCamelCase(str: string): string {
    return str.replace(/(?:^\w|[A-Z]|\b\w)/g, (word, index) => {
      return index === 0 ? word.toLowerCase() : word.toUpperCase();
    }).replace(/\s+/g, '');
  }

  openModal() {
    this.modalOpened = true;
    this.objectFields = this.headers.map(header => ({label: header, value: ''}));
    this.title = 'Add ' + this.title;
    this.selectedItemId = null;
    this.showModal = true;
    this.orderItems = [];
    this.userAddress = { id: '', city: '', streetName: '', houseNumber: '', zipCode: '' };
  }

  openUpdateModal(id: any) {
    this.modalOpened = true;
    this.isUpdating = true;
    const selectedItem = this.data.find((item: any) => item.id === id);
    if (selectedItem) {
      this.title = 'Update ' + this.title;
      this.objectFields = this.headers.map(header => ({
        label: header,
        value: selectedItem[this.toCamelCase(header)] || ''
      }));

      this.orderItems = selectedItem.orderItems || [];
      this.userAddress = selectedItem.address || { streetName: '', houseNumber: '', city: '', zipCode: '' };

      this.selectedItemId = id;
      this.showModal = true;
    }
  }

  closeModal() {
    this.showModal = false;
    this.modalOpened = false;
    this.isUpdating = false;
  }

  saveObject() {
    const newObject: any = {};
    this.objectFields.forEach(field => {
      newObject[this.toCamelCase(field.label)] = field.value;
    });

    if (this.title.endsWith('User')) {
      newObject.address = {
        city: this.userAddress.city,
        streetName: this.userAddress.streetName,
        houseNumber: this.userAddress.houseNumber,
        zipCode: this.userAddress.zipCode
      };
    }

    if (this.title.endsWith('Order')) {
      newObject.orderItems = this.orderItems.map(orderItem => ({
        productName: orderItem.productName,
        quantity: orderItem.quantity
      }));
    }

    console.log("New object: ", newObject)
    this.saveObjectEvent.emit(newObject);
    this.closeModal();
  }

  updateObject() {
    const selectedItem = this.data.find((item: any) => item.id === this.selectedItemId);
    if (selectedItem) {
      this.objectFields.forEach(field => {
        if (field.label !== 'Order Items' && field.label !== 'Address') {
          selectedItem[this.toCamelCase(field.label)] = field.value;
        }
      });

      if (this.title.endsWith('Order')) {
        selectedItem.orderItems = this.orderItems.map(orderItem => ({
          productName: orderItem.productName,
          quantity: orderItem.quantity
        }));
      }

      if (this.title.endsWith('User')) {
        selectedItem.address = {
          city: this.userAddress.city,
          streetName: this.userAddress.streetName,
          houseNumber: this.userAddress.houseNumber,
          zipCode: this.userAddress.zipCode
        };
      }

      console.log("New object: ", selectedItem)
      this.updateObjectEvent.emit(selectedItem);
    }
    this.closeModal();
  }

  openConfirmationModal(id: string) {
    this.selectedItemId = id;
    this.modalOpened = true;
    this.showConfirmationModal = true;
    this.data.find((item: any) => item.id === id);
  }

  deleteObject() {
    const selectedItem = this.data.find((item:any) => item.id === this.selectedItemId);
    if (selectedItem) {
      this.deleteObjectEvent.emit(selectedItem);
    }
    this.closeModal();
  }

  confirmDeletion() {
    this.deleteObject();
    this.closeConfirmationModal();
  }

  closeConfirmationModal() {
    this.modalOpened = false;
    this.showConfirmationModal = false;

  }

  addOrderItem() {
    this.orderItems.push({ productName: '', quantity: 1 });
  }

  removeOrderItem(index: number) {
    this.orderItems.splice(index, 1);
  }

  formatDate(expirationDate: number[]): string {
    const [year, month, day, hours, minutes, seconds] = expirationDate;

    let formattedDate = new Date(year, month - 1, day);

    if (!isNaN(hours)) {
      formattedDate.setHours(hours);
    }

    if (!isNaN(minutes)) {
      formattedDate.setMinutes(minutes);
    }

    if (!isNaN(seconds)) {
      formattedDate.setSeconds(seconds);
    }

    return formattedDate.toLocaleString();
  }
}
