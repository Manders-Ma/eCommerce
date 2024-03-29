import { Component, OnInit } from '@angular/core';
import { PaymentService } from '../../services/payment.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-confirm-pay',
  templateUrl: './confirm-pay.component.html',
  styleUrl: './confirm-pay.component.css'
})
export class ConfirmPayComponent implements OnInit {

  private transactionId!: string;
  private orderTrackingNumber!: string;
  private storage: Storage = sessionStorage;

  constructor(
    private paymentService: PaymentService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.transactionId = params['transactionId'];
      this.orderTrackingNumber = params['orderId'];
      this.confirm();
    });
  }

  confirm() {
    this.paymentService.confirm(this.transactionId, this.orderTrackingNumber).subscribe({
      next: response => {
        alert("付款成功");
        this.router.navigateByUrl("/order-history");
      },
      error: err => {
        alert("付款失敗");
        this.router.navigateByUrl("/order-history");
      }
    });
  }
}
