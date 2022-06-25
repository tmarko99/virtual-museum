import { TestBed } from '@angular/core/testing';

import { EksponatiService } from './eksponati.service';

describe('EksponatiService', () => {
  let service: EksponatiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EksponatiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
