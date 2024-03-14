import {Component, Input} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {SidebarComponent} from "../../shared/sidebar/sidebar.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SidebarComponent],
  templateUrl: './product.component.html',
  styleUrl: './product.component.css'
})

export class ProductComponent {
  title = 'Products Management';
  @Input() data: any;
}
