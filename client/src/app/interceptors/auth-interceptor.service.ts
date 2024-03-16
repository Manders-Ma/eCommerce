import { HttpErrorResponse, HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { Member } from '../common/member';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment.development';
import { LoginService } from '../services/login.service';
import { AppConstants } from '../constants/app-constants';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor {

  member!: Member;

  constructor(private router: Router, private loginService: LoginService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Only add an access token for secured endpoints
    const securedEndpoints = [
      AppConstants.LOGIN_URL,
      AppConstants.PRODUCT_URL
    ];

    if (securedEndpoints.some(url => req.urlWithParams.includes(url))) {
      let httpHeaders = new HttpHeaders();
      if (sessionStorage.getItem('memberDetails')) {
        this.member = JSON.parse(sessionStorage.getItem('memberDetails')!);
      }
      if (this.member && this.member.password && this.member.email) {
        httpHeaders = httpHeaders.append('Authorization', 'Basic ' + window.btoa(this.member.email + ':' + this.member.password));
      }
      let xsrf = sessionStorage.getItem('XSRF-TOKEN');
      if (xsrf) {
        httpHeaders = httpHeaders.append('X-XSRF-TOKEN', xsrf);
      }
      httpHeaders = httpHeaders.append('X-Requested-With', 'XMLHttpRequest');
      req = req.clone({
        headers: httpHeaders
      });
    }
    return next.handle(req);
  }
}
