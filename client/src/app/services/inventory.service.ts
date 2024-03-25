import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AppConstants } from '../constants/app-constants';
import { Observable } from 'rxjs';
import { Product } from '../common/product';

@Injectable({
  providedIn: 'root'
})
export class InventoryService {

  private inventoryUrl = AppConstants.INVENTORY_URL;

  constructor(private httpClient: HttpClient) { }

  deleteProductById(theProductId: number): Observable<any> {
    const deleteUrl = `${this.inventoryUrl}/delete-product/${theProductId}`
    return this.httpClient.delete(deleteUrl, { observe: 'response', withCredentials: true });
  }

  updateProduct(theProduct: Product): Observable<any> {
    const updateUrl = `${this.inventoryUrl}/update-product`;
    return this.httpClient.patch(updateUrl, theProduct, { observe: 'response', withCredentials: true });
  }
}
