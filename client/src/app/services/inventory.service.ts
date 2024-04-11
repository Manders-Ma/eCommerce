import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AppConstants } from '../constants/app-constants';
import { Observable } from 'rxjs';
import { Product } from '../common/product';
import { ProductCreation } from '../common/product-creation';

@Injectable({
  providedIn: 'root'
})
export class InventoryService {

  private inventoryUrl = AppConstants.INVENTORY_URL;

  constructor(private httpClient: HttpClient) { }

  deleteProductById(theProductId: number): Observable<any> {
    return this.httpClient.delete(this.inventoryUrl + `/${theProductId}`, { observe: 'response', withCredentials: true });
  }

  updateProduct(theProduct: Product): Observable<any> {
    return this.httpClient.patch(this.inventoryUrl, theProduct, { observe: 'response', withCredentials: true });
  }

  saveProduct(productCreation: ProductCreation): Observable<any> {
    return this.httpClient.post(this.inventoryUrl, productCreation, { observe: 'response', withCredentials: true });
  }
}
