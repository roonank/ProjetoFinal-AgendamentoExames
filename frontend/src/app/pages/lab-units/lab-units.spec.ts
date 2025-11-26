import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LabUnits } from './lab-units';

describe('LabUnits', () => {
  let component: LabUnits;
  let fixture: ComponentFixture<LabUnits>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LabUnits]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LabUnits);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
