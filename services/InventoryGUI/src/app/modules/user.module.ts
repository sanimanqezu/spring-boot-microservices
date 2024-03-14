import {Address} from "./address.module";

export interface User {
  id: string;
  firstName: string;
  lastName: string;
  rsaId: string;
  dateOfBirth: Date;
  address: Address;
}
