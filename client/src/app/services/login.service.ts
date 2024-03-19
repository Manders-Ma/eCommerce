import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Member } from '../common/member';
import { BehaviorSubject, Subject, catchError, map } from 'rxjs';
import { AppConstants } from '../constants/app-constants';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  loginUrl: string = AppConstants.LOGIN_URL;
  storage: Storage = sessionStorage;
  isAuthenticated: Subject<boolean> = new BehaviorSubject<boolean>(false);
  member: Member = new Member();

  constructor(private httpClient: HttpClient) {
    if (this.storage.getItem("isAuthenticated")) {
      this.successAuthentication();
    }
  }

  validateLoginDetails(member: Member) {
    this.member = member;
    return this.httpClient.get<Member>(this.loginUrl, { observe: "response", withCredentials: true });
  }

  successAuthentication() {
    this.isAuthenticated.next(true);
  }

  resetAuthenticationState() {
    this.isAuthenticated.next(false);
  }


}
