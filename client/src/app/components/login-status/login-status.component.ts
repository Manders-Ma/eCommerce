import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { Member } from '../../common/member';
import { removeCookie } from 'typescript-cookie';

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
    // 登入登出都做過一次之後，再登入如果沒清掉XSRF-TOKEN，spring會認為你有所以不會給你，
    // 由於我目前只在login的時候才獲取cookie並儲存到session stotage，所以這次沒拿到會
    // 導致之後沒有辦法下訂單。
    removeCookie("XSRF-TOKEN");
    this.storage.clear();
  }
}
