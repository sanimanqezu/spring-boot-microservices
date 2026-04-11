import { Component, EventEmitter, Input, OnChanges, Output } from '@angular/core';
import { NgClass, NgForOf, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrderItem } from '../../../modules/OrderItem.module';
import { Address } from '../../../modules/address.module';

@Component({
  selector: 'app-data-table',
  standalone: true,
  imports: [NgIf, NgForOf, NgClass, FormsModule],
  templateUrl: './data-table.component.html',
  styleUrl: './data-table.component.css'
})
export class DataTableComponent implements OnChanges {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  @Input() data: any[] = [];
  @Input() title: string = '';
  @Input() showModal: boolean = false;
  @Input() objectFields: { label: string; value: string }[] = [];
  @Input() headers: string[] = [];
  @Input() isLoading: boolean = false;

  @Output() saveObjectEvent = new EventEmitter<any>();
  @Output() updateObjectEvent = new EventEmitter<any>();
  @Output() deleteObjectEvent = new EventEmitter<any>();

  // Modal state
  selectedItemId: string | null = null;
  showConfirmationModal: boolean = false;
  modalOpened: boolean = false;
  isUpdating: boolean = false;
  orderItems: OrderItem[] = [];
  userAddress: Address = { id: '', city: '', streetName: '', houseNumber: '', zipCode: '' };
  validationError: string = '';

  // Filter & pagination
  filterText: string = '';
  currentPage: number = 1;
  pageSize: number = 10;
  filteredData: any[] = [];

  private originalTitle: string = '';

  ngOnChanges(): void {
    this.applyFilter();
  }

  applyFilter(): void {
    const term = this.filterText.toLowerCase().trim();
    if (!term) {
      this.filteredData = [...(this.data ?? [])];
    } else {
      this.filteredData = (this.data ?? []).filter(item =>
        Object.values(item).some(val =>
          val !== null && val !== undefined && String(val).toLowerCase().includes(term)
        )
      );
    }
    this.currentPage = 1;
  }

  get totalPages(): number {
    return Math.max(1, Math.ceil(this.filteredData.length / this.pageSize));
  }

  get pagedData(): any[] {
    const start = (this.currentPage - 1) * this.pageSize;
    return this.filteredData.slice(start, start + this.pageSize);
  }

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i + 1);
  }

  prevPage(): void {
    if (this.currentPage > 1) this.currentPage--;
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) this.currentPage++;
  }

  goToPage(page: number): void {
    this.currentPage = page;
  }

  toCamelCase(str: string): string {
    return str.replace(/(?:^\w|[A-Z]|\b\w)/g, (word, index) => {
      return index === 0 ? word.toLowerCase() : word.toUpperCase();
    }).replace(/\s+/g, '');
  }

  openModal(): void {
    this.originalTitle = this.title;
    this.modalOpened = true;
    this.isUpdating = false;
    this.validationError = '';
    this.objectFields = this.headers.map(h => ({ label: h, value: '' }));
    this.selectedItemId = null;
    this.showModal = true;
    this.orderItems = [];
    this.userAddress = { id: '', city: '', streetName: '', houseNumber: '', zipCode: '' };
  }

  openUpdateModal(id: string): void {
    this.originalTitle = this.title;
    this.modalOpened = true;
    this.isUpdating = true;
    this.validationError = '';
    const selectedItem = this.data.find(item => item['id'] === id);
    if (selectedItem) {
      this.objectFields = this.headers.map(h => ({
        label: h,
        value: String(selectedItem[this.toCamelCase(h)] ?? '')
      }));
      this.orderItems = (selectedItem['orderItems'] as OrderItem[]) ?? [];
      this.userAddress = (selectedItem['address'] as Address) ?? { id: '', streetName: '', houseNumber: '', city: '', zipCode: '' };
      this.selectedItemId = id;
      this.showModal = true;
    }
  }

  closeModal(): void {
    this.showModal = false;
    this.modalOpened = false;
    this.isUpdating = false;
    this.validationError = '';
    if (this.originalTitle) {
      this.title = this.originalTitle;
    }
  }

  private validate(): boolean {
    for (const field of this.objectFields) {
      if (field.label === 'Order Items' || field.label === 'Address') continue;
      if (!field.value || String(field.value).trim() === '') {
        this.validationError = `${field.label} is required.`;
        return false;
      }
    }
    const idNoField = this.objectFields.find(f => f.label === 'Orderer Id No');
    if (idNoField && idNoField.value.length !== 13) {
      this.validationError = 'Orderer Id No must be exactly 13 digits.';
      return false;
    }
    this.validationError = '';
    return true;
  }

  saveObject(): void {
    if (!this.validate()) return;

    const newObject: any = {};
    this.objectFields.forEach(field => {
      newObject[this.toCamelCase(field.label)] = field.value;
    });

    if (this.title.endsWith('User') || this.title === 'Add User' || this.title === 'Update User') {
      newObject['address'] = { ...this.userAddress };
    }
    if (this.title.endsWith('Order') || this.title === 'Add Order' || this.title === 'Update Order') {
      newObject['orderItems'] = this.orderItems.map(oi => ({
        productName: oi.productName,
        quantity: oi.quantity
      }));
    }

    this.saveObjectEvent.emit(newObject);
    this.closeModal();
  }

  updateObject(): void {
    if (!this.validate()) return;

    const selectedItem = this.data.find(item => item['id'] === this.selectedItemId);
    if (selectedItem) {
      const updated = { ...selectedItem };
      this.objectFields.forEach(field => {
        if (field.label !== 'Order Items' && field.label !== 'Address') {
          updated[this.toCamelCase(field.label)] = field.value;
        }
      });

      if (this.title.endsWith('Order') || this.title === 'Update Order') {
        updated['orderItems'] = this.orderItems.map(oi => ({
          productName: oi.productName,
          quantity: oi.quantity
        }));
      }
      if (this.title.endsWith('User') || this.title === 'Update User') {
        updated['address'] = { ...this.userAddress };
      }

      this.updateObjectEvent.emit(updated);
    }
    this.closeModal();
  }

  openConfirmationModal(id: string): void {
    this.selectedItemId = id;
    this.modalOpened = true;
    this.showConfirmationModal = true;
  }

  confirmDeletion(): void {
    const selectedItem = this.data.find(item => item['id'] === this.selectedItemId);
    if (selectedItem) {
      this.deleteObjectEvent.emit(selectedItem);
    }
    this.closeConfirmationModal();
  }

  closeConfirmationModal(): void {
    this.modalOpened = false;
    this.showConfirmationModal = false;
    this.selectedItemId = null;
  }

  addOrderItem(): void {
    this.orderItems.push({ productName: '', quantity: 1 });
  }

  removeOrderItem(index: number): void {
    this.orderItems.splice(index, 1);
  }
}
