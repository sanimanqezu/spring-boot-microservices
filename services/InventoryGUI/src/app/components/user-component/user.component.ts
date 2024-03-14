import {Component, Input} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {SidebarComponent} from "../../shared/sidebar/sidebar.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SidebarComponent],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css'
})

export class UserComponent {
  title = 'Users Management';
  @Input() data: any;
}
