import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { Product } from '../../common/product';
import { ActivatedRoute } from '@angular/router';
import { Member } from '../../common/member';
import { AppConstants } from '../../constants/app-constants';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent implements OnInit {

  products: Product[] = [];
  currentCategoryId: number = 1;
  previousCategoryId: number = 1;
  searchMode: boolean = false;
  previousKeyword: string = "";

  // set properties for pagination 
  thePageNumber: number = 1;
  thePageSize: number = 10;
  theTotalElements: number = 0;

  // access client's role
  storage: Storage = sessionStorage;
  member: Member = new Member();

  // The current active route that loaded the component.
  // Useful for accessing the route parameter.
  constructor(private productService: ProductService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    if (this.storage.getItem("memberDetails")) {
      this.member = JSON.parse(this.storage.getItem("memberDetails")!);
    }
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

    // if we have a different keyword than previous
    // we set thePageNumber back to 1
    if (this.previousKeyword != theKeyword) {
      console.log(`Changing search keyword from ${this.previousKeyword} to ${theKeyword}.` +
        `\tWe set thePageNumber back to 1.`);
      this.thePageNumber = 1;
    }

    this.previousKeyword = theKeyword;

    // search for the products using keyword
    this.productService.searchProductsPaginate(
      this.thePageNumber - 1,
      this.thePageSize,
      theKeyword
    ).subscribe(this.processResult());
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

    // if we have a different category id than previous
    // we set thePageNumber back to 1
    if (this.previousCategoryId != this.currentCategoryId) {
      console.log(`Changing catrgory id from ${this.previousCategoryId} to ${this.currentCategoryId}.`
        + `\tWe set thePageNumber back to 1.`);
      this.thePageNumber = 1;
    }

    this.previousCategoryId = this.currentCategoryId;

    // Pagination component: pages are 1 based
    // Spring data rest: pages are 0 based
    this.productService.getProductListPaginate(
      this.thePageNumber - 1,
      this.thePageSize,
      this.currentCategoryId
    ).subscribe(this.processResult());
  }

  private processResult() {
    return (data: any) => {
      this.products = data._embedded.products;
      this.thePageNumber = data.page.number + 1;
      this.thePageSize = data.page.size;
      this.theTotalElements = data.page.totalElements
    };
  }

  updatePageSize(pageSize: string) {
    this.thePageNumber = 1;
    this.thePageSize = Number(pageSize);
    this.listProducts();
  }

}
