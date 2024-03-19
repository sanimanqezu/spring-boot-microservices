import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class RequestService {

  constructor(private http: HttpClient) { }

  postForObject<T>(url: string, body: any, options?: { headers?: HttpHeaders | { [header: string]: string | string[] },
    params?: HttpParams | { [param: string]: string | string[] } }): Observable<T> {
    return this.http.post<T>(url, body, options);
  }

  getForObject<T>(url: string, options?: { headers?: HttpHeaders | { [header: string]: string | string[] },
    params?: HttpParams | { [param: string]: string | string[] } }): Observable<T> {
    return this.http.get<T>(url, options);
  }

  putForObject<T>(url: string, body: any, options?: { headers?: HttpHeaders | { [header: string]: string | string[] },
    params?: HttpParams | { [param: string]: string | string[] } }): Observable<T> {
    return this.http.put<T>(url, body, options);
  }

  deleteForObject<T>(url: string, options?: { headers?: HttpHeaders | { [header: string]: string | string[] },
    params?: HttpParams | { [param: string]: string | string[] } }): Observable<T> {
    return this.http.delete<T>(url, options);
  }

  encodeParams(params: { [param: string]: string }): string {
    return Object.keys(params)
      .map(key => encodeURIComponent(key) + '=' + encodeURIComponent(params[key]))
      .join('&');
  }
}
