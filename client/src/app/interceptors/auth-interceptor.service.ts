import { HttpErrorResponse, HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { Member } from '../common/member';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor {

  member!: Member;

  constructor(private router: Router) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Only add an access token for secured endpoints
    const securedEndpoints = [
      "http://localhost:8080/member/details",
      environment.apiUrl + "/checkout/purchase"
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
