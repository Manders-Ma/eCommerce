import { Component, OnInit } from '@angular/core';
import { OrderHistoryService } from '../../services/order-history.service';
import { OrderHistory } from '../../common/order-history';

@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html',
  styleUrl: './order-history.component.css'
})
export class OrderHistoryComponent implements OnInit {

  orderHistories: OrderHistory[] = [];

  constructor(private orderHistoryService: OrderHistoryService) { }

  ngOnInit(): void {
    this.orderHistoryService.getOrderHistory().subscribe(
      data => {
        console.log(data);
        this.orderHistories = data;
      }
    );
  }

}
