import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../attendence-students-record.test-samples';

import { AttendenceStudentsRecordFormService } from './attendence-students-record-form.service';

describe('AttendenceStudentsRecord Form Service', () => {
  let service: AttendenceStudentsRecordFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AttendenceStudentsRecordFormService);
  });

  describe('Service methods', () => {
    describe('createAttendenceStudentsRecordFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAttendenceStudentsRecordFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            attendenceId: expect.any(Object),
            studentId: expect.any(Object),
            studentName: expect.any(Object),
            isPresent: expect.any(Object),
            createdAt: expect.any(Object),
            createdBy: expect.any(Object),
          }),
        );
      });

      it('passing IAttendenceStudentsRecord should create a new form with FormGroup', () => {
        const formGroup = service.createAttendenceStudentsRecordFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            attendenceId: expect.any(Object),
            studentId: expect.any(Object),
            studentName: expect.any(Object),
            isPresent: expect.any(Object),
            createdAt: expect.any(Object),
            createdBy: expect.any(Object),
          }),
        );
      });
    });

    describe('getAttendenceStudentsRecord', () => {
      it('should return NewAttendenceStudentsRecord for default AttendenceStudentsRecord initial value', () => {
        const formGroup = service.createAttendenceStudentsRecordFormGroup(sampleWithNewData);

        const attendenceStudentsRecord = service.getAttendenceStudentsRecord(formGroup) as any;

        expect(attendenceStudentsRecord).toMatchObject(sampleWithNewData);
      });

      it('should return NewAttendenceStudentsRecord for empty AttendenceStudentsRecord initial value', () => {
        const formGroup = service.createAttendenceStudentsRecordFormGroup();

        const attendenceStudentsRecord = service.getAttendenceStudentsRecord(formGroup) as any;

        expect(attendenceStudentsRecord).toMatchObject({});
      });

      it('should return IAttendenceStudentsRecord', () => {
        const formGroup = service.createAttendenceStudentsRecordFormGroup(sampleWithRequiredData);

        const attendenceStudentsRecord = service.getAttendenceStudentsRecord(formGroup) as any;

        expect(attendenceStudentsRecord).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAttendenceStudentsRecord should not enable id FormControl', () => {
        const formGroup = service.createAttendenceStudentsRecordFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAttendenceStudentsRecord should disable id FormControl', () => {
        const formGroup = service.createAttendenceStudentsRecordFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
