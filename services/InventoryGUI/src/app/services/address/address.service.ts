import { Injectable } from '@angular/core';
import {RequestService} from "../request/request.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AddressService {

  baseUrl = "http://localhost:7002/address-service/address"

  constructor(private requestService: RequestService) { }

  addAddress(body: any) {
    const url = this.baseUrl;
    this.requestService.postForObject<any>(url, body).subscribe(response => {
      console.log(response);
    });
  }

  getAllAddresses(): Observable<any> {
    const url = this.baseUrl;
    return this.requestService.getForObject<any>(url);
  }

  encodeParams() {
    const params = { key1: 'value1', key2: 'value2' };
    const encodedParams = this.requestService.encodeParams(params);
    console.log(encodedParams);
  }
}
