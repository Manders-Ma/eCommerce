import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { InventoryService } from '../../services/inventory.service';
import { ProductService } from '../../services/product.service';
import { Product } from '../../common/product';

@Component({
  selector: 'app-product-details-modify',
  templateUrl: './product-details-modify.component.html',
  styleUrl: './product-details-modify.component.css'
})
export class ProductDetailsModifyComponent implements OnInit {

  product!: Product;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private inventoryService: InventoryService,
    private productService: ProductService
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(() => {
      this.handleProductDetails();
    });
  }

  handleProductDetails() {
    const theProductId: number = Number(this.route.snapshot.paramMap.get("id"));

    this.productService.getProduct(theProductId).subscribe(
      data => {
        this.product = data;
      }
    )
  }

  update() {
    this.inventoryService.updateProduct(this.product).subscribe(
      {
        next: response => {
          alert(response.body.message);
        },
        error: err => {
          alert(err.body.message);
        }
      }
    )
  }

}
