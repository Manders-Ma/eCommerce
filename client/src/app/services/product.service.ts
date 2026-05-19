import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Product } from '../common/product';
import { environment } from '../../environments/environment.development';
import { ProductCategory } from '../common/product-category';
import { AppConstants } from '../constants/app-constants';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private productUrl = AppConstants.PRODUCT_URL;
  private categoryUrl = AppConstants.CATEGORY_URL;

  constructor(private httpClient: HttpClient) { }

  getProduct(theProductId: number): Observable<Product> {
    const searchUrl = `${this.productUrl}/${theProductId}`;
    return this.httpClient.get<Product>(searchUrl);
  }

  getProductListPaginate(thePage: number, thePageSize: number, theCategoryId: number): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}/search/findByCategoryId?id=${theCategoryId}`
      + `&page=${thePage}&size=${thePageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }

  searchProductsPaginate(thePage: number, thePageSize: number, theKeyword: string): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}/search/findByNameContaining?name=${theKeyword}`
      + `&page=${thePage}&size=${thePageSize}`;
    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }

  getProductCategories(): Observable<ProductCategory[]> {
    return this.httpClient.get<GetResponseProductCategory>(this.categoryUrl).pipe(
      map(response => response.embedded.productCategory)
    );
  }
}

interface GetResponseProducts {
  embedded: {
    products: Product[];
  },
  page: {
    size: number,
    totalElements: number,
    totalPages: number,
    number: number
  }
}

interface GetResponseProductCategory {
  embedded: {
    productCategory: ProductCategory[];
  }
}
