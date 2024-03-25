import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { InventoryService } from '../../services/inventory.service';
import { ProductService } from '../../services/product.service';
import { Product } from '../../common/product';
import { ProductCategory } from '../../common/product-category';
import { ProductCreation } from '../../common/product-creation';

@Component({
  selector: 'app-product-create',
  templateUrl: './product-create.component.html',
  styleUrl: './product-create.component.css'
})
export class ProductCreateComponent implements OnInit {

  product: Product = new Product();
  productCategories: ProductCategory[] = [];
  selectedProductCategory!: ProductCategory;

  constructor(
    private router: Router,
    private inventoryService: InventoryService,
    private productService: ProductService
  ) {

  }

  ngOnInit(): void {
    // 初始圖片先用這個代替
    this.product.imageUrl = "assets/images/products/books/book-luv2code-1000.png";
    this.product.active = true;

    this.productService.getProductCategories().subscribe(
      data => {
        this.productCategories = data;
      }
    );
  }

  save() {
    let productCreation: ProductCreation = new ProductCreation();
    productCreation.product = this.product;
    productCreation.productCategory = this.selectedProductCategory;
    console.log(productCreation);
    this.inventoryService.saveProduct(productCreation).subscribe(
      {
        next: response => {
          alert(response.body.message);
          this.router.navigateByUrl("/");
        },
        error: err => {
          alert(err.body.message);
        }
      }
    );
  }

}
