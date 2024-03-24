import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Member } from '../common/member';
import { BehaviorSubject, Subject, catchError, map } from 'rxjs';
import { AppConstants } from '../constants/app-constants';
import { removeCookie } from 'typescript-cookie';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  loginUrl: string = AppConstants.LOGIN_URL;
  storage: Storage = sessionStorage;
  isAuthenticated: Subject<boolean> = new BehaviorSubject<boolean>(false);
  member: Member = new Member();

  constructor(private httpClient: HttpClient, private router: Router) {
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

  logout(jump: boolean = false) {
    this.resetAuthenticationState();
    // 登入登出都做過一次之後，再登入如果沒清掉XSRF-TOKEN，spring會認為你有所以不會給你，
    // 由於我目前只在login的時候才獲取cookie並儲存到session stotage，所以這次沒拿到會
    // 導致之後沒有辦法下訂單。
    removeCookie("XSRF-TOKEN");
    const deletedKey: string[] = ["memberDetails", "isAuthenticated", "Authorization", "XSRF-TOKEN"];
    deletedKey.forEach(key => this.storage.removeItem(key));

    if (jump) {
      this.router.navigateByUrl("/login");
    }
    else {
      this.router.navigateByUrl("/logout-page");
    }
  }

}
