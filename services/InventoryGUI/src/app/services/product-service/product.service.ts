import {Injectable} from '@angular/core';
import {RequestService} from "../request-service/request.service";
import {map, Observable} from "rxjs";
import {Product} from "../../modules/product.module";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  baseUrl = "http://localhost:7002/product-service/products"

  constructor(private requestService: RequestService) { }

  addProduct(body: Product): Observable<Product> {
    console.log("Body: ", body)
    const url = this.baseUrl;
    return this.requestService.postForObject<Product>(url, body)
      .pipe(
        map((response: Product) => response)
      );
  }

  getAllProducts(): Observable<Product[]> {
    const url = this.baseUrl;
    return this.requestService.getForObject<Product[]>(url)
      .pipe(
        map((response: Product[]) => response)
      );
  }

  updateProduct(id: string, body: Product): Observable<Product[]> {
    const url = `${this.baseUrl}/id?id=${id}`;
    return this.requestService.putForObject<Product[]>(url, body);
  }

  deleteProduct(id: string): Observable<Product[]> {
    const url = `${this.baseUrl}/id?id=${id}`;
    return this.requestService.deleteForObject<Product[]>(url)
      .pipe(
        map((response: Product[]) => response)
      );
  }
}
