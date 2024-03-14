import {Component, Input} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {SidebarComponent} from "../../shared/sidebar/sidebar.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SidebarComponent],
  templateUrl: './order.component.html',
  styleUrl: './order.component.css'
})

export class OrderComponent {
  title = 'Orders Management';
  @Input() data: any;
}
