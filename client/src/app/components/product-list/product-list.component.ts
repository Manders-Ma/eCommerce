import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { Product } from '../../common/product';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent implements OnInit {

  products: Product[] = [];
  currentCategoryId!: number;
  searchMode: boolean = false;

  // The current active route that loaded the component.
  // Useful for accessing the route parameter.
  constructor(private productService: ProductService,
    private route: ActivatedRoute) { }

  ngOnInit(): void {
    // ngOnInit初次載入才會呼叫這些函數, 因此用subscribe讓他每次都能變更。
    this.route.paramMap.subscribe(() => {
      this.listProducts();
    });
    this.listProducts();
  }

  listProducts() {
    this.searchMode = this.route.snapshot.paramMap.has("keyword");

    if (this.searchMode) {
      this.handleSearchProducts();
    }
    else {
      this.handleListProducts();
    }
  }
  handleSearchProducts() {
    // add ! to avoid "Type 'null' is not assignable to type 'string'."
    const theKeyword: string = this.route.snapshot.paramMap.get("keyword")!;

    // search for the products using keyword
    this.productService.searchProducts(theKeyword).subscribe(
      data => {
        this.products = data;
      }
    )
  }

  handleListProducts() {
    // check if "id" parameter is available.
    // @snapshot -> state of route at this given moment in time.
    // @paramMap -> map of all the route parameters.
    const hasCategoryId: boolean = this.route.snapshot.paramMap.has('id');

    if (hasCategoryId) {
      this.currentCategoryId = Number(this.route.snapshot.paramMap.get('id'));
    }
    else {
      // if not category id available, we use default category id = 1.
      this.currentCategoryId = 1;
    }

    this.productService.getProductList(this.currentCategoryId).subscribe(
      data => {
        this.products = data;
      }
    )
  }

}
