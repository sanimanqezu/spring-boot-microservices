import { TestBed } from '@angular/core/testing';
import { ProductComponent } from './product.component';

describe('ProductComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductComponent],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(ProductComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have the 'InventoryGUI' title`, () => {
    const fixture = TestBed.createComponent(ProductComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('InventoryGUI');
  });

  it('should render title', () => {
    const fixture = TestBed.createComponent(ProductComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Hello, InventoryGUI');
  });
});
