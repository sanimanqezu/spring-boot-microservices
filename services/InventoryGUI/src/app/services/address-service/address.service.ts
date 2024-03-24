import {Injectable} from '@angular/core';
import {RequestService} from "../request-service/request.service";
import {map, Observable} from "rxjs";
import {Address} from "../../modules/address.module";

@Injectable({
  providedIn: 'root'
})
export class AddressService {

  baseUrl = "http://localhost:7002/address-service/addresses"

  constructor(private requestService: RequestService) { }

  addAddress(body: Address): Observable<Address> {
    console.log("Body: ", body)
    const url = this.baseUrl;
    return this.requestService.postForObject<Address>(url, body)
      .pipe(
        map((response: Address) => response)
      );
  }

  getAllAddresses(): Observable<Address[]> {
    const url = this.baseUrl;
    return this.requestService.getForObject<Address[]>(url)
      .pipe(
        map((response: Address[]) => response)
      );
  }

  updateAddress(id: string, body: Address): Observable<Address[]> {
    const url = `${this.baseUrl}/id?id=${id}`;
    return this.requestService.putForObject<Address[]>(url, body);
  }

  deleteAddress(id: string): Observable<Address[]> {
    const url = `${this.baseUrl}/id?id=${id}`;
    return this.requestService.deleteForObject<Address[]>(url)
      .pipe(
        map((response: Address[]) => response)
      );
  }
}
