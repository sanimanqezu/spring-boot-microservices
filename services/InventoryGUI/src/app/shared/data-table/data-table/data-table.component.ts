import {Component, EventEmitter, Input, Output} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-data-table',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    FormsModule
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
    this.data.push(newObject);
    this.saveObjectEvent.emit(newObject);
    this.closeModal();
  }

  updateObject() {
    const selectedItem = this.data.find((item:any) => item.id === this.selectedItemId);
    if (selectedItem) {
      this.objectFields.forEach(field => {
        selectedItem[this.toCamelCase(field.label)] = field.value;
        this.updateObjectEvent.emit(selectedItem);
      });
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
}
