import {Component, OnDestroy, OnInit} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {SidebarComponent} from "../../shared/sidebar/sidebar.component";
import {DataTableComponent} from "../../shared/data-table/data-table/data-table.component";
import {AddressService} from "../../services/address/address.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SidebarComponent, DataTableComponent],
  templateUrl: './address.component.html',
  styleUrl: './address.component.css'
})

export class AddressComponent implements OnInit, OnDestroy {
  title = 'Addresses Management';
  headers: string[] = ['City', 'Street Name', 'House Number', 'Zip Code'];
  myData: any;
  addressSubscription: Subscription | undefined;

  constructor(private addressService: AddressService) {}

  ngOnInit() {
    this.getAllAddresses();
  }

  getAllAddresses() {
    this.addressSubscription = this.addressService.getAllAddresses()
      .subscribe(response => {
      this.myData = response;
        console.log("All addresses: ", this.myData)
    });
  }

  ngOnDestroy() {
    if (this.addressSubscription) {
      this.addressSubscription.unsubscribe();
    }
  }
}
