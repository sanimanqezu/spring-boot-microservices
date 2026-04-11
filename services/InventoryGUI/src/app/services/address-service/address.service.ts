import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RequestService } from '../request-service/request.service';
import { Address } from '../../modules/address.module';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AddressService {

  private readonly baseUrl = `${environment.apiUrl}/address-service/addresses`;

  constructor(private requestService: RequestService) {}

  addAddress(body: Address): Observable<Address> {
    return this.requestService.postForObject<Address>(this.baseUrl, body);
  }

  getAllAddresses(): Observable<Address[]> {
    return this.requestService.getForObject<Address[]>(this.baseUrl);
  }

  updateAddress(id: string, body: Address): Observable<void> {
    return this.requestService.putForObject<void>(`${this.baseUrl}/id?id=${id}`, body);
  }

  deleteAddress(id: string): Observable<void> {
    return this.requestService.deleteForObject<void>(`${this.baseUrl}/id?id=${id}`);
  }
}
