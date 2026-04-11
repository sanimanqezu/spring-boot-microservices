import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RequestService } from '../request-service/request.service';
import { Product } from '../../modules/product.module';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ProductService {

  private readonly baseUrl = `${environment.apiUrl}/product-service/products`;

  constructor(private requestService: RequestService) {}

  addProduct(body: Product): Observable<Product> {
    return this.requestService.postForObject<Product>(this.baseUrl, body);
  }

  getAllProducts(): Observable<Product[]> {
    return this.requestService.getForObject<Product[]>(this.baseUrl);
  }

  updateProduct(id: string, body: Product): Observable<void> {
    return this.requestService.putForObject<void>(`${this.baseUrl}/id?id=${id}`, body);
  }

  deleteProduct(id: string): Observable<void> {
    return this.requestService.deleteForObject<void>(`${this.baseUrl}/id?id=${id}`);
  }
}
