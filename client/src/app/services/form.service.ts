import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { Observable, map } from 'rxjs';
import { ShippingAddress } from '../common/shipping-address';

@Injectable({
  providedIn: 'root'
})
export class FormService {

  private shippingAddressUrl = environment.apiUrl + "/shipping-address";

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