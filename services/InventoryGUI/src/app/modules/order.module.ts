import {OrderItem} from "./OrderItem.module";

export interface Order {
  id: string;
  orderNumber: string;
  ordererFullName: string;
  ordererIdNo: string;
  quantity: string;
  orderItems: OrderItem[];
}
