import { TestBed } from '@angular/core/testing';
import { OrderComponent } from './order.component';

describe('OrderComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrderComponent],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(OrderComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have the 'InventoryGUI' title`, () => {
    const fixture = TestBed.createComponent(OrderComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('InventoryGUI');
  });

  it('should render title', () => {
    const fixture = TestBed.createComponent(OrderComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Hello, InventoryGUI');
  });
});
