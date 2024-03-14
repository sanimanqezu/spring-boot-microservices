import {Component, Input} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-data-table',
  standalone: true,
  imports: [
    NgIf,
    NgForOf
  ],
  templateUrl: './data-table.component.html',
  styleUrl: './data-table.component.css'
})
export class DataTableComponent {
  showModal: any;
  showConfirmationModal: any;
  @Input() data: any;
  @Input() headers!: string[];

  openModal() {

  }

  openUpdateModal(id: any) {

  }

  openConfirmationModal(id: any) {

  }
}
