import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../assignment-file.test-samples';

import { AssignmentFileFormService } from './assignment-file-form.service';

describe('AssignmentFile Form Service', () => {
  let service: AssignmentFileFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AssignmentFileFormService);
  });

  describe('Service methods', () => {
    describe('createAssignmentFileFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAssignmentFileFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            studentId: expect.any(Object),
            assignmentId: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            fileSize: expect.any(Object),
            createdBy: expect.any(Object),
            createdAt: expect.any(Object),
            modifiedAt: expect.any(Object),
            mimeType: expect.any(Object),
            extension: expect.any(Object),
            binaryData: expect.any(Object),
            markingStatus: expect.any(Object),
            grade: expect.any(Object),
            feedback: expect.any(Object),
            gradedBy: expect.any(Object),
            gradedAt: expect.any(Object),
          }),
        );
      });

      it('passing IAssignmentFile should create a new form with FormGroup', () => {
        const formGroup = service.createAssignmentFileFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            studentId: expect.any(Object),
            assignmentId: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            fileSize: expect.any(Object),
            createdBy: expect.any(Object),
            createdAt: expect.any(Object),
            modifiedAt: expect.any(Object),
            mimeType: expect.any(Object),
            extension: expect.any(Object),
            binaryData: expect.any(Object),
            markingStatus: expect.any(Object),
            grade: expect.any(Object),
            feedback: expect.any(Object),
            gradedBy: expect.any(Object),
            gradedAt: expect.any(Object),
          }),
        );
      });
    });

    describe('getAssignmentFile', () => {
      it('should return NewAssignmentFile for default AssignmentFile initial value', () => {
        const formGroup = service.createAssignmentFileFormGroup(sampleWithNewData);

        const assignmentFile = service.getAssignmentFile(formGroup) as any;

        expect(assignmentFile).toMatchObject(sampleWithNewData);
      });

      it('should return NewAssignmentFile for empty AssignmentFile initial value', () => {
        const formGroup = service.createAssignmentFileFormGroup();

        const assignmentFile = service.getAssignmentFile(formGroup) as any;

        expect(assignmentFile).toMatchObject({});
      });

      it('should return IAssignmentFile', () => {
        const formGroup = service.createAssignmentFileFormGroup(sampleWithRequiredData);

        const assignmentFile = service.getAssignmentFile(formGroup) as any;

        expect(assignmentFile).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAssignmentFile should not enable id FormControl', () => {
        const formGroup = service.createAssignmentFileFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAssignmentFile should disable id FormControl', () => {
        const formGroup = service.createAssignmentFileFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
