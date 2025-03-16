import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../campus-event.test-samples';

import { CampusEventFormService } from './campus-event-form.service';

describe('CampusEvent Form Service', () => {
  let service: CampusEventFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CampusEventFormService);
  });

  describe('Service methods', () => {
    describe('createCampusEventFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCampusEventFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            eventName: expect.any(Object),
            description: expect.any(Object),
            eventDate: expect.any(Object),
            location: expect.any(Object),
            organizerId: expect.any(Object),
            eventType: expect.any(Object),
            capacity: expect.any(Object),
            status: expect.any(Object),
          }),
        );
      });

      it('passing ICampusEvent should create a new form with FormGroup', () => {
        const formGroup = service.createCampusEventFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            eventName: expect.any(Object),
            description: expect.any(Object),
            eventDate: expect.any(Object),
            location: expect.any(Object),
            organizerId: expect.any(Object),
            eventType: expect.any(Object),
            capacity: expect.any(Object),
            status: expect.any(Object),
          }),
        );
      });
    });

    describe('getCampusEvent', () => {
      it('should return NewCampusEvent for default CampusEvent initial value', () => {
        const formGroup = service.createCampusEventFormGroup(sampleWithNewData);

        const campusEvent = service.getCampusEvent(formGroup) as any;

        expect(campusEvent).toMatchObject(sampleWithNewData);
      });

      it('should return NewCampusEvent for empty CampusEvent initial value', () => {
        const formGroup = service.createCampusEventFormGroup();

        const campusEvent = service.getCampusEvent(formGroup) as any;

        expect(campusEvent).toMatchObject({});
      });

      it('should return ICampusEvent', () => {
        const formGroup = service.createCampusEventFormGroup(sampleWithRequiredData);

        const campusEvent = service.getCampusEvent(formGroup) as any;

        expect(campusEvent).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICampusEvent should not enable id FormControl', () => {
        const formGroup = service.createCampusEventFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCampusEvent should disable id FormControl', () => {
        const formGroup = service.createCampusEventFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
