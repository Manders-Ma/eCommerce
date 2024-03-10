import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { CartItem } from '../common/cart-item';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  cartItems: CartItem[] = [];

  // Subject is a subclass of Observable.
  // 我們可以使用他來推送事件。該事件將會送到所有訂閱者。
  // BehaviorSubject is a subclass of Subject
  // 會推送最新事件給剛加入的訂閱者
  totalPrice: Subject<number> = new BehaviorSubject<number>(0);
  totalQuantity: Subject<number> = new BehaviorSubject<number>(0);

  constructor() { }

  addToCart(theCartItem: CartItem) {

    // check if we already have the item in our cart
    let alreadyExistsInCart: boolean = false;
    let existingCartItem!: CartItem;

    if (this.cartItems.length > 0) {
      // find the item in the cart based on item id
      for (let tempCartItem of this.cartItems) {
        if (tempCartItem.id === theCartItem.id) {
          alreadyExistsInCart = true;
          existingCartItem = tempCartItem;
          break;
        }
      }
    }

    // check if we found it
    if (alreadyExistsInCart) {
      existingCartItem.quantity++;
    }
    else {
      this.cartItems.push(theCartItem);
    }

    // compute cart total price and total quantity
    this.computeCartTotals();
  }

  computeCartTotals() {
    let totalPriceValue: number = 0;
    let totalQuantityValue: number = 0;

    console.log("@ Content of the cart: ");
    for (let cartItem of this.cartItems) {
      totalPriceValue += cartItem.unitPrice * cartItem.quantity;
      totalQuantityValue += cartItem.quantity;
      console.log(`name: ${cartItem.name}, unitPrice= ${cartItem.unitPrice}, quantity= ${cartItem.quantity}`);
    }
    console.log("----------------------");

    // publish the new values
    // all subscribers will receive the new datas
    this.totalPrice.next(totalPriceValue);
    this.totalQuantity.next(totalQuantityValue);
  }

  decrementQuantity(theCartItem: CartItem) {
    theCartItem.quantity--;

    if (theCartItem.quantity == 0) {
      this.remove(theCartItem);
    }
    else {
      this.computeCartTotals();
    }
  }

  private remove(theCartItem: CartItem) {
    const itemIdx = this.cartItems.findIndex(tempCartItem => theCartItem.id == tempCartItem.id);

    if (itemIdx > -1) {
      this.cartItems.splice(itemIdx, 1);
      this.computeCartTotals();
    }
  }
}
