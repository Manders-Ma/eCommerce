import { Component, DoCheck, OnInit } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { Member } from '../../common/member';
import { removeCookie } from 'typescript-cookie';

@Component({
  selector: 'app-login-status',
  templateUrl: './login-status.component.html',
  styleUrl: './login-status.component.css'
})
export class LoginStatusComponent implements OnInit, DoCheck {

  isAuthenticated: boolean = false;
  storage: Storage = sessionStorage;
  member: Member = new Member();

  constructor(private loginService: LoginService) { }

  ngOnInit(): void {
    if (this.storage.getItem("memberDetails")) {
      this.member = JSON.parse(this.storage.getItem("memberDetails")!);
    }
    this.loginService.isAuthenticated.subscribe(data => this.isAuthenticated = data);
  }

  ngDoCheck(): void {
    // 檢查是否有新的 memberDetails
    if (this.storage.getItem("memberDetails")) {
      this.member = JSON.parse(this.storage.getItem("memberDetails")!);
    }
  }

  logout() {
    this.loginService.logout();
  }
}
