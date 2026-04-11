import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RequestService } from '../request-service/request.service';
import { User } from '../../modules/user.module';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class UserService {

  private readonly baseUrl = `${environment.apiUrl}/user-service/users`;

  constructor(private requestService: RequestService) {}

  addUser(body: User): Observable<User> {
    return this.requestService.postForObject<User>(this.baseUrl, body);
  }

  getAllUsers(): Observable<User[]> {
    return this.requestService.getForObject<User[]>(this.baseUrl);
  }

  updateUser(id: string, body: User): Observable<void> {
    return this.requestService.putForObject<void>(`${this.baseUrl}/id?id=${id}`, body);
  }

  deleteUser(id: string): Observable<void> {
    return this.requestService.deleteForObject<void>(`${this.baseUrl}/id?id=${id}`);
  }
}
