import { Component, Input, OnChanges } from '@angular/core';
import { NgIf, NgClass } from '@angular/common';

@Component({
  selector: 'app-alert',
  standalone: true,
  imports: [NgIf, NgClass],
  templateUrl: './alert.component.html',
  styleUrl: './alert.component.css'
})
export class AlertComponent implements OnChanges {
  @Input() message: string = '';
  @Input() type: 'success' | 'error' | 'warning' | 'info' = 'error';

  show: boolean = false;
  private dismissTimer: ReturnType<typeof setTimeout> | null = null;

  ngOnChanges(): void {
    if (this.message) {
      this.show = true;
      if (this.dismissTimer) {
        clearTimeout(this.dismissTimer);
      }
      this.dismissTimer = setTimeout(() => {
        this.show = false;
      }, 4000);
    } else {
      this.show = false;
    }
  }

  dismiss(): void {
    this.show = false;
  }
}
