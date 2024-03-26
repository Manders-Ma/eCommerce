import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AppConstants } from '../constants/app-constants';
import { Observable } from 'rxjs';
import { OrderHistory } from '../common/order-history';

@Injectable({
  providedIn: 'root'
})
export class OrderHistoryService {

  private orderHistoryUrl = AppConstants.ORDER_HISTORY_URL;

  constructor(private httpClient: HttpClient) { }

  getOrderHistory(): Observable<OrderHistory[]> {
    return this.httpClient.get<OrderHistory[]>(this.orderHistoryUrl);
  }
}
