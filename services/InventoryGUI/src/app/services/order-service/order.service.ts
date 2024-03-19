import {Injectable} from '@angular/core';
import {RequestService} from "../request-service/request.service";
import {map, Observable} from "rxjs";
import {Order} from "../../modules/order.module";

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  baseUrl = "http://localhost:7002/order-service/orders"

  constructor(private requestService: RequestService) { }

  addOrder(body: Order) {
    const url = this.baseUrl;
    return this.requestService.postForObject<Order>(url, body)
      .pipe(
        map((response: Order) => response)
      );
  }

  getAllOrders(): Observable<Order[]> {
    const url = this.baseUrl;
    return this.requestService.getForObject<Order[]>(url)
      .pipe(
        map((response: Order[]) => response)
      );
  }

  updateOrder(id: string, body: Order): Observable<Order[]> {
    const url = `${this.baseUrl}/id?id=${id}`;
    return this.requestService.putForObject<Order[]>(url, body);
  }

  deleteOrder(id: string): Observable<Order[]> {
    const url = `${this.baseUrl}/id?id=${id}`;
    return this.requestService.deleteForObject<Order[]>(url)
      .pipe(
        map((response: Order[]) => response)
      );
  }
}
