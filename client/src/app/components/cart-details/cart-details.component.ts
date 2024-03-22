import { Component, OnInit } from '@angular/core';
import { CartItem } from '../../common/cart-item';
import { CartService } from '../../services/cart.service';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-cart-details',
  templateUrl: './cart-details.component.html',
  styleUrl: './cart-details.component.css'
})
export class CartDetailsComponent implements OnInit {

  cartItems: CartItem[] = [];
  totalPrice: number = 0;
  totalQuantity: number = 0;

  constructor(private cartService: CartService, private productService: ProductService) { }

  ngOnInit(): void {
    this.listCartDetails();
  }

  listCartDetails() {
    // 更新存貨數量
    this.cartService.cartItems.forEach(item => {
      this.productService.getProduct(item.id).subscribe(
        data => item.unitsInStock = data.unitsInStock
      )
    });

    this.cartItems = this.cartService.cartItems;

    // subscribe to the cart totalPrice and totalQuantity
    this.cartService.totalPrice.subscribe(data => this.totalPrice = data);
    this.cartService.totalQuantity.subscribe(data => this.totalQuantity = data);
  }

  incrementQuantity(theCartItem: CartItem) {
    this.cartService.addToCart(theCartItem);
  }

  decrementQuantity(theCartItem: CartItem) {
    this.cartService.decrementQuantity(theCartItem);
  }
}
