import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RequestService } from '../request-service/request.service';
import { Order } from '../../modules/order.module';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class OrderService {

  private readonly baseUrl = `${environment.apiUrl}/order-service/orders`;

  constructor(private requestService: RequestService) {}

  addOrder(body: Order): Observable<void> {
    return this.requestService.postForObject<void>(this.baseUrl, body);
  }

  getAllOrders(): Observable<Order[]> {
    return this.requestService.getForObject<Order[]>(this.baseUrl);
  }

  updateOrder(id: string, body: Order): Observable<void> {
    return this.requestService.putForObject<void>(`${this.baseUrl}/id?id=${id}`, body);
  }

  deleteOrder(id: string): Observable<void> {
    return this.requestService.deleteForObject<void>(`${this.baseUrl}/id?id=${id}`);
  }
}
