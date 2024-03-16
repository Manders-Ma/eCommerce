import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Member } from '../common/member';
import { BehaviorSubject, Subject, catchError, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  loginUrl: string = "http://localhost:8080/member/details";
  storage: Storage = sessionStorage;
  isAuthenticated: Subject<boolean> = new BehaviorSubject<boolean>(false);

  constructor(private httpClient: HttpClient) { }

  validateLoginDetails(member: Member) {
    this.storage.setItem("memberDetails", JSON.stringify(member));
    return this.httpClient.get<Member>(this.loginUrl, { withCredentials: true });
  }

  successAuthentication() {
    this.isAuthenticated.next(true);
  }

  resetAuthenticationState() {
    this.isAuthenticated.next(false);
  }


}
