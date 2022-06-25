import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EksponatDetaljiComponent } from './eksponat-detalji.component';

describe('EksponatDetaljiComponent', () => {
  let component: EksponatDetaljiComponent;
  let fixture: ComponentFixture<EksponatDetaljiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EksponatDetaljiComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EksponatDetaljiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
