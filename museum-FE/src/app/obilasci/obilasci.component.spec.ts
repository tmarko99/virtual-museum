import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObilasciComponent } from './obilasci.component';

describe('ObilasciComponent', () => {
  let component: ObilasciComponent;
  let fixture: ComponentFixture<ObilasciComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ObilasciComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ObilasciComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
