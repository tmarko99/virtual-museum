import { TestBed } from '@angular/core/testing';

import { ObilazakService } from './obilazak.service';

describe('ObilazakService', () => {
  let service: ObilazakService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ObilazakService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
