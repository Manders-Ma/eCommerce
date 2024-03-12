import { Customer } from "./customer";
import { Order } from "./order";
import { OrderItem } from "./order-item";
import { ShippingAddress } from "./shipping-address";

export class Purchase {

    public customer!: Customer;
    public shippingAddress!: ShippingAddress;
    public order!: Order;
    public orderItems!: OrderItem[];

    constructor() { }
}
