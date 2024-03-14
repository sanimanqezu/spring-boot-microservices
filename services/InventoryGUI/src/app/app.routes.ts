import {Routes} from '@angular/router';
import {UserComponent} from "./components/user-component/user.component";
import {ProductComponent} from "./components/product-component/product.component";
import {AddressComponent} from "./components/address-component/address.component";
import {OrderComponent} from "./components/order-component/order.component";
import {DashboardComponent} from "./components/dashboard/dashboard.component";

export const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'user', component: UserComponent },
  { path: 'product', component: ProductComponent },
  { path: 'order', component: OrderComponent },
  { path: 'address', component: AddressComponent },
];
