import { Customer } from "./customer";

export class OrderHistory {
    public id!: number;
    public orderTrackingNumber!: number;
    public totalPrice!: number;
    public totalQuantity!: number;
    public status!: string;
    public dateCreated!: Date;
    public customer!: Customer

    constructor() { }
}
