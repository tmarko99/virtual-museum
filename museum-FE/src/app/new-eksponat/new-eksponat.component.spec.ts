import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewEksponatComponent } from './new-eksponat.component';

describe('NewEksponatComponent', () => {
  let component: NewEksponatComponent;
  let fixture: ComponentFixture<NewEksponatComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewEksponatComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewEksponatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
