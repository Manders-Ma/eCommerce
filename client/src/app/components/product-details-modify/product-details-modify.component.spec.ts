import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductDetailsModifyComponent } from './product-details-modify.component';

describe('ProductDetailsModifyComponent', () => {
  let component: ProductDetailsModifyComponent;
  let fixture: ComponentFixture<ProductDetailsModifyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProductDetailsModifyComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ProductDetailsModifyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
