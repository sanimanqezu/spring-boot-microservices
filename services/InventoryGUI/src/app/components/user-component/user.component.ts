import {Component, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {SidebarComponent} from "../../shared/sidebar/sidebar.component";
import {DataTableComponent} from "../../shared/data-table/data-table/data-table.component";
import {Subscription} from "rxjs";
import {UserService} from "../../services/user-service/user.service";
import {User} from "../../modules/user.module";
import {AlertComponent} from "../../shared/alert/alert.component";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SidebarComponent, DataTableComponent, AlertComponent, NgIf],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css'
})

export class UserComponent implements OnInit, OnDestroy {

  @ViewChild(DataTableComponent) dataTableComponent!: DataTableComponent;
  title: string = 'User';
  headers: string[] = ['First Name', 'Last Name', 'Rsa Id', 'Date Of Birth', 'Address'];
  myData: User[] = [];
  userSubscription: Subscription | undefined;
  dataTableObjectFields!: { label: string; value: string }[];
  dataTableShowModal: boolean = false;
  errorMessage: string = '';

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.getAllUsers();
  }

  getAllUsers() {
    this.userSubscription = this.userService.getAllUsers()
      .subscribe(
        response => {
          this.myData = response;
          console.log("All users: ", this.myData);
        },
        error => {
          console.error("Error retrieving users:", error.error);
          this.errorMessage = "Failed to retrieve users. Please try again later.";
        }
      );
  }

  handleSave(newObject: any) {
    this.userService.addUser(newObject)
      .subscribe(
        response => {
          console.log("Post response: ", response)
          console.log("All users after post request: ", this.myData)
        },
        error => {
          console.error("Error adding user:", error.error);
          this.errorMessage = "Failed to add user. Please try again later.";
        }
      );
  }

  handleUpdate(selectedObject: any) {
    const objectId = selectedObject.id;
    this.userService.updateUser(objectId, selectedObject)
      .subscribe(
        response => {
          console.log('User updated successfully:', response);
          this.getAllUsers();
        },
        error => {
          console.error('Error updating user:', error.error);
          this.errorMessage = "Failed to update user. Please try again later.";
        }
      );
  }

  handleDelete(selectedObject: any) {
    const objectId = selectedObject.id;
    this.userService.deleteUser(objectId)
      .subscribe(
        response => {
          console.log("User deleted successfully: ", response)
          this.getAllUsers();
        },
        error => {
          console.error("Error deleting user:", error.error);
          this.errorMessage = "Failed to delete user. Please try again later.";
        }
      );
  }

  ngOnDestroy() {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }
}
