import { TestBed } from '@angular/core/testing';
import { AddressComponent } from './address.component';

describe('AddressComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddressComponent],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AddressComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have the 'InventoryGUI' title`, () => {
    const fixture = TestBed.createComponent(AddressComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('InventoryGUI');
  });

  it('should render title', () => {
    const fixture = TestBed.createComponent(AddressComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Hello, InventoryGUI');
  });
});
