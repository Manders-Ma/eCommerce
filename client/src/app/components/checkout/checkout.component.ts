import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { CartService } from '../../services/cart.service';
import { ShippingAddress } from '../../common/shipping-address';
import { FormService } from '../../services/form.service';
import { CheckoutService } from '../../services/checkout.service';
import { Router } from '@angular/router';
import { Order } from '../../common/order';
import { OrderItem } from '../../common/order-item';
import { Purchase } from '../../common/purchase';
import { CustomValidators } from '../../validators/custom-validators';
import { Member } from '../../common/member';
import { LoginService } from '../../services/login.service';
import { HttpErrorResponse } from '@angular/common/http';
import { BehaviorSubject, Subject } from 'rxjs';
import { PaymentService } from '../../services/payment.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css'
})
export class CheckoutComponent implements OnInit {

  storage: Storage = sessionStorage;

  totalPrice: number = 0;
  totalQuantity: number = 0;

  shippingAddress: ShippingAddress[] = [];

  checkoutFormGroup!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private cartService: CartService,
    private formService: FormService,
    private checkoutService: CheckoutService,
    private loginService: LoginService,
    private router: Router,
    private paymentService: PaymentService
  ) { }

  ngOnInit(): void {
    this.reviewCartDetails();

    this.checkoutFormGroup = this.formBuilder.group({
      customer: this.formBuilder.group({
        firstName: new FormControl('',
          [Validators.required, Validators.minLength(2), CustomValidators.notOnlyWhitespace]),
        lastName: new FormControl('',
          [Validators.required, Validators.minLength(2), CustomValidators.notOnlyWhitespace]),
        email: new FormControl('',
          [Validators.required, Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$')]
        ),
      }),
      shippingAddress: this.formBuilder.group({
        address: new FormControl('', Validators.required)
      })
    });

    this.formService.getShippingAddress().subscribe(
      data => {
        this.shippingAddress = data;
      }
    );
  }

  onSubmit() {
    console.log("Handling the submit button");
    console.log(this.checkoutFormGroup.value);

    if (this.checkoutFormGroup.invalid) {
      this.checkoutFormGroup.markAllAsTouched();
      return;
    }

    // set up member
    const member: Member = JSON.parse(this.storage.getItem("memberDetails")!);

    // set up order
    let order: Order = new Order(this.totalQuantity, this.totalPrice);

    // get cart item
    const cartItems = this.cartService.cartItems;

    // create orderItems from cartItems
    let orderItems: OrderItem[] = cartItems.map(item => new OrderItem(item));

    // set up purchase
    let purchase = new Purchase();

    // populate purchase - member
    purchase.member = member;

    // populate purchase - customer
    purchase.customer = this.checkoutFormGroup.get("customer")?.value;

    // populate purchase - order and orderItems
    purchase.order = order;
    purchase.orderItems = orderItems;

    // populate purchase - shipping address
    let shippingAddressId = this.checkoutFormGroup.get("shippingAddress.address")?.value.id;
    let shippingAddressName = this.checkoutFormGroup.get("shippingAddress.address")?.value.name;
    let shippingAddressAddress = this.checkoutFormGroup.get("shippingAddress.address")?.value.address;
    purchase.shippingAddress = new ShippingAddress(shippingAddressId, shippingAddressName, shippingAddressAddress);

    // log purchase data
    console.log("our purchae info: ");
    console.log(purchase);

    // call REST API via the checkoutService
    this.checkoutService.placeOrder(purchase).subscribe(
      {
        next: response => {
          console.log(response);
          alert(`Your order has been received.\norder tracking number = ${response.orderTrackingNumber}`);
          this.resetCart();
          this.paymentService.request(response.orderTrackingNumber).subscribe(data => {
            window.open(data.message, "_self");
          });
        },
        error: err => {
          console.log(err);
          if (err.status === 406) {
            alert("你所選的商品數量不足或已完售，請再次確認購物車。");
            this.router.navigateByUrl("/cart-details");
          }
          else if (err.status === 405) {
            alert("訂購人資料已存在。");
            this.router.navigateByUrl("/cart-details");
          }
          else if (err.status === 401) {
            alert("登入憑證已過期，請再次進行登入。");
            this.loginService.logout(true);
          }
          else if (err.status === 403) {
            alert("使用者權限不足");
          }
        }
      }
    );
  }

  resetCart() {
    // reset cart data
    this.cartService.cartItems = [];
    this.cartService.persistCartItems();
    this.cartService.totalPrice.next(0);
    this.cartService.totalQuantity.next(0);

    // reset the form
    this.checkoutFormGroup.reset();
  }

  reviewCartDetails() {
    this.cartService.totalPrice.subscribe(data => this.totalPrice = data);
    this.cartService.totalQuantity.subscribe(data => this.totalQuantity = data);
  }

  // getter functions for form control
  get firstName() { return this.checkoutFormGroup.get("customer.firstName"); }
  get lastName() { return this.checkoutFormGroup.get("customer.lastName"); }
  get email() { return this.checkoutFormGroup.get("customer.email"); }
  get address() { return this.checkoutFormGroup.get("shippingAddress.address"); }
}
