import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../folder.test-samples';

import { FolderFormService } from './folder-form.service';

describe('Folder Form Service', () => {
  let service: FolderFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FolderFormService);
  });

  describe('Service methods', () => {
    describe('createFolderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFolderFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            contents: expect.any(Object),
            courseId: expect.any(Object),
            course: expect.any(Object),
            semester: expect.any(Object),
            createdBy: expect.any(Object),
            createdAt: expect.any(Object),
            modifiedAt: expect.any(Object),
            parentId: expect.any(Object),
          }),
        );
      });

      it('passing IFolder should create a new form with FormGroup', () => {
        const formGroup = service.createFolderFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            contents: expect.any(Object),
            courseId: expect.any(Object),
            course: expect.any(Object),
            semester: expect.any(Object),
            createdBy: expect.any(Object),
            createdAt: expect.any(Object),
            modifiedAt: expect.any(Object),
            parentId: expect.any(Object),
          }),
        );
      });
    });

    describe('getFolder', () => {
      it('should return NewFolder for default Folder initial value', () => {
        const formGroup = service.createFolderFormGroup(sampleWithNewData);

        const folder = service.getFolder(formGroup) as any;

        expect(folder).toMatchObject(sampleWithNewData);
      });

      it('should return NewFolder for empty Folder initial value', () => {
        const formGroup = service.createFolderFormGroup();

        const folder = service.getFolder(formGroup) as any;

        expect(folder).toMatchObject({});
      });

      it('should return IFolder', () => {
        const formGroup = service.createFolderFormGroup(sampleWithRequiredData);

        const folder = service.getFolder(formGroup) as any;

        expect(folder).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFolder should not enable id FormControl', () => {
        const formGroup = service.createFolderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFolder should disable id FormControl', () => {
        const formGroup = service.createFolderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
