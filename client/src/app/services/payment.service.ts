import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppConstants } from '../constants/app-constants';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  private requestUrl = AppConstants.REQUEST_URL;
  private confirmUrl = AppConstants.CONFIRM_URL;

  constructor(private htppClient: HttpClient) { }

  request(orderTrackingNumber: string): Observable<any> {
    return this.htppClient.post(this.requestUrl, orderTrackingNumber, { withCredentials: true });
  }

  confirm(transactionId: string, orderTrackingNumber: string): Observable<any> {
    const body = {
      "transactionId": transactionId,
      "orderTrackingNumber": orderTrackingNumber
    };
    return this.htppClient.post(this.confirmUrl, body, { observe: "response", withCredentials: true });
  }
}
