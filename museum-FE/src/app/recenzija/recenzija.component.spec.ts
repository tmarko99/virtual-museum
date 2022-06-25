import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecenzijaComponent } from './recenzija.component';

describe('RecenzijaComponent', () => {
  let component: RecenzijaComponent;
  let fixture: ComponentFixture<RecenzijaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RecenzijaComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RecenzijaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
