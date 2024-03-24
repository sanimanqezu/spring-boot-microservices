import {Component, Input, OnInit} from '@angular/core';
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-alert',
  standalone: true,
  imports: [
    NgIf
  ],
  templateUrl: './alert.component.html',
  styleUrl: './alert.component.css'
})
export class AlertComponent implements OnInit {
  @Input() message: string = '';
  show: boolean = false;

  ngOnInit() {
    this.show = true;
    setTimeout(() => {
      this.show = false;
    }, 3000); // 3 seconds
  }
}
