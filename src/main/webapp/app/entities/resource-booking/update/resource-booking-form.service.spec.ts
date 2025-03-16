import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../resource-booking.test-samples';

import { ResourceBookingFormService } from './resource-booking-form.service';

describe('ResourceBooking Form Service', () => {
  let service: ResourceBookingFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResourceBookingFormService);
  });

  describe('Service methods', () => {
    describe('createResourceBookingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createResourceBookingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userId: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            status: expect.any(Object),
            reason: expect.any(Object),
            adminComment: expect.any(Object),
            resource: expect.any(Object),
          }),
        );
      });

      it('passing IResourceBooking should create a new form with FormGroup', () => {
        const formGroup = service.createResourceBookingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userId: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            status: expect.any(Object),
            reason: expect.any(Object),
            adminComment: expect.any(Object),
            resource: expect.any(Object),
          }),
        );
      });
    });

    describe('getResourceBooking', () => {
      it('should return NewResourceBooking for default ResourceBooking initial value', () => {
        const formGroup = service.createResourceBookingFormGroup(sampleWithNewData);

        const resourceBooking = service.getResourceBooking(formGroup) as any;

        expect(resourceBooking).toMatchObject(sampleWithNewData);
      });

      it('should return NewResourceBooking for empty ResourceBooking initial value', () => {
        const formGroup = service.createResourceBookingFormGroup();

        const resourceBooking = service.getResourceBooking(formGroup) as any;

        expect(resourceBooking).toMatchObject({});
      });

      it('should return IResourceBooking', () => {
        const formGroup = service.createResourceBookingFormGroup(sampleWithRequiredData);

        const resourceBooking = service.getResourceBooking(formGroup) as any;

        expect(resourceBooking).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IResourceBooking should not enable id FormControl', () => {
        const formGroup = service.createResourceBookingFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewResourceBooking should disable id FormControl', () => {
        const formGroup = service.createResourceBookingFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
