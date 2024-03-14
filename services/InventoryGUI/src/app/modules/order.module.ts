export interface Order {
  id: string;
  orderNumber: string;
  quantity: number;
  products: { [productName: string]: string };
  ordererFullName: string;
  ordererIdNo: string;
}
