import {Injectable} from '@angular/core';
import {RequestService} from "../request-service/request.service";
import {map, Observable} from "rxjs";
import {User} from "../../modules/user.module";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  baseUrl = "http://localhost:7002/user-service/users"

  constructor(private requestService: RequestService) { }

  addUser(body: User): Observable<User> {
    console.log("Body: ", body)
    const url = this.baseUrl;
    return this.requestService.postForObject<User>(url, body)
      .pipe(
        map((response: User) => response)
      );
  }

  getAllUsers(): Observable<User[]> {
    const url = this.baseUrl;
    return this.requestService.getForObject<User[]>(url)
      .pipe(
        map((response: User[]) => response)
      );
  }

  updateUser(id: string, body: User): Observable<User[]> {
    const url = `${this.baseUrl}/id?id=${id}`;
    return this.requestService.putForObject<User[]>(url, body);
  }

  deleteUser(id: string): Observable<User[]> {
    const url = `${this.baseUrl}/id?id=${id}`;
    return this.requestService.deleteForObject<User[]>(url)
      .pipe(
        map((response: User[]) => response)
      );
  }
}
