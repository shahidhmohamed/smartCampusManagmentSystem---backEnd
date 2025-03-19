import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../attendence.test-samples';

import { AttendenceFormService } from './attendence-form.service';

describe('Attendence Form Service', () => {
  let service: AttendenceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AttendenceFormService);
  });

  describe('Service methods', () => {
    describe('createAttendenceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAttendenceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdAt: expect.any(Object),
            courseId: expect.any(Object),
            courseName: expect.any(Object),
            instructorId: expect.any(Object),
            instructorName: expect.any(Object),
            moduleId: expect.any(Object),
            moduleName: expect.any(Object),
          }),
        );
      });

      it('passing IAttendence should create a new form with FormGroup', () => {
        const formGroup = service.createAttendenceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdAt: expect.any(Object),
            courseId: expect.any(Object),
            courseName: expect.any(Object),
            instructorId: expect.any(Object),
            instructorName: expect.any(Object),
            moduleId: expect.any(Object),
            moduleName: expect.any(Object),
          }),
        );
      });
    });

    describe('getAttendence', () => {
      it('should return NewAttendence for default Attendence initial value', () => {
        const formGroup = service.createAttendenceFormGroup(sampleWithNewData);

        const attendence = service.getAttendence(formGroup) as any;

        expect(attendence).toMatchObject(sampleWithNewData);
      });

      it('should return NewAttendence for empty Attendence initial value', () => {
        const formGroup = service.createAttendenceFormGroup();

        const attendence = service.getAttendence(formGroup) as any;

        expect(attendence).toMatchObject({});
      });

      it('should return IAttendence', () => {
        const formGroup = service.createAttendenceFormGroup(sampleWithRequiredData);

        const attendence = service.getAttendence(formGroup) as any;

        expect(attendence).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAttendence should not enable id FormControl', () => {
        const formGroup = service.createAttendenceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAttendence should disable id FormControl', () => {
        const formGroup = service.createAttendenceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
