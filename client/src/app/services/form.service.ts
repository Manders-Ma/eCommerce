import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { ShippingAddress } from '../common/shipping-address';
import { AppConstants } from '../constants/app-constants';

@Injectable({
  providedIn: 'root'
})
export class FormService {

  private shippingAddressUrl = AppConstants.SHIPPING_ADDRESS_URL

  constructor(private httpClient: HttpClient) { }

  getShippingAddress(): Observable<ShippingAddress[]> {
    return this.httpClient.get<GetResponseShippingAddress>(this.shippingAddressUrl).pipe(
      map(response => response._embedded.shippingAddress)
    );
  }
}

interface GetResponseShippingAddress {
  _embedded: {
    shippingAddress: ShippingAddress[]
  }
}