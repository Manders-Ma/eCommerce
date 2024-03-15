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

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css'
})
export class CheckoutComponent implements OnInit {

  totalPrice: number = 0;
  totalQuantity: number = 0;

  shippingAddress: ShippingAddress[] = [];

  checkoutFormGroup!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private cartService: CartService,
    private formService: FormService,
    private checkoutService: CheckoutService,
    private router: Router
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

    // set up order
    let order: Order = new Order(this.totalQuantity, this.totalPrice);

    // get cart item
    const cartItems = this.cartService.cartItems;

    // create orderItems from cartItems
    let orderItems: OrderItem[] = cartItems.map(item => new OrderItem(item));

    // set up purchase
    let purchase = new Purchase();

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
          alert(`Your order has been received.\norder tracking number = ${response.orderTrackingNumber}`);

          this.resetCart();

        },
        error: err => {
          alert(`There was an error: ${err.message}`);
        }
      }
    );
  }

  resetCart() {
    // reset cart data
    this.cartService.cartItems = [];
    this.cartService.totalPrice.next(0);
    this.cartService.totalQuantity.next(0);

    // reset the form
    this.checkoutFormGroup.reset();

    // navigate back to the product page
    this.router.navigateByUrl("/products");
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
