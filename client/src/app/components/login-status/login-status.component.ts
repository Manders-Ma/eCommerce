import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { Member } from '../../common/member';

@Component({
  selector: 'app-login-status',
  templateUrl: './login-status.component.html',
  styleUrl: './login-status.component.css'
})
export class LoginStatusComponent implements OnInit {

  isAuthenticated: boolean = false;
  storage: Storage = sessionStorage;

  constructor(private loginService: LoginService) { }

  ngOnInit(): void {
    this.loginService.isAuthenticated.subscribe(data => this.isAuthenticated = data);
  }

  logout() {
    this.loginService.resetAuthenticationState();
    this.loginService.member = new Member();
    this.storage.setItem("XSRF-TOKEN", "");
  }
}
