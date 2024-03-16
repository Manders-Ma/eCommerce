import { Customer } from "./customer";
import { Member } from "./member";
import { Order } from "./order";
import { OrderItem } from "./order-item";
import { ShippingAddress } from "./shipping-address";

export class Purchase {

    public member!: Member;
    public customer!: Customer;
    public shippingAddress!: ShippingAddress;
    public order!: Order;
    public orderItems!: OrderItem[];

    constructor() { }
}
