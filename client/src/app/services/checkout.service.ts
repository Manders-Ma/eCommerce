import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Purchase } from '../common/purchase';
import { Observable } from 'rxjs';
import { AppConstants } from '../constants/app-constants';

@Injectable({
  providedIn: 'root'
})
export class CheckoutService {

  private purchaseUrl: string = AppConstants.PURCHASE_URL;

  constructor(private httpClient: HttpClient) { }

  placeOrder(purchase: Purchase): Observable<any> {
    return this.httpClient.post<Purchase>(this.purchaseUrl, purchase, { withCredentials: true });
  }
}
