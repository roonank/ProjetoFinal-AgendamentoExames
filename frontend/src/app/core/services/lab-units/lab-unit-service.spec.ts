import { TestBed } from '@angular/core/testing';

import { LabUnitService } from './lab-unit-service';

describe('LabUnitService', () => {
  let service: LabUnitService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LabUnitService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
