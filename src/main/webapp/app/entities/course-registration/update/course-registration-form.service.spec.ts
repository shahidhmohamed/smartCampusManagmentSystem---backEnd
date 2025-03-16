import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../course-registration.test-samples';

import { CourseRegistrationFormService } from './course-registration-form.service';

describe('CourseRegistration Form Service', () => {
  let service: CourseRegistrationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CourseRegistrationFormService);
  });

  describe('Service methods', () => {
    describe('createCourseRegistrationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCourseRegistrationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            studentId: expect.any(Object),
            courseId: expect.any(Object),
            courseCode: expect.any(Object),
            duration: expect.any(Object),
            registrationDate: expect.any(Object),
          }),
        );
      });

      it('passing ICourseRegistration should create a new form with FormGroup', () => {
        const formGroup = service.createCourseRegistrationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            studentId: expect.any(Object),
            courseId: expect.any(Object),
            courseCode: expect.any(Object),
            duration: expect.any(Object),
            registrationDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getCourseRegistration', () => {
      it('should return NewCourseRegistration for default CourseRegistration initial value', () => {
        const formGroup = service.createCourseRegistrationFormGroup(sampleWithNewData);

        const courseRegistration = service.getCourseRegistration(formGroup) as any;

        expect(courseRegistration).toMatchObject(sampleWithNewData);
      });

      it('should return NewCourseRegistration for empty CourseRegistration initial value', () => {
        const formGroup = service.createCourseRegistrationFormGroup();

        const courseRegistration = service.getCourseRegistration(formGroup) as any;

        expect(courseRegistration).toMatchObject({});
      });

      it('should return ICourseRegistration', () => {
        const formGroup = service.createCourseRegistrationFormGroup(sampleWithRequiredData);

        const courseRegistration = service.getCourseRegistration(formGroup) as any;

        expect(courseRegistration).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICourseRegistration should not enable id FormControl', () => {
        const formGroup = service.createCourseRegistrationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCourseRegistration should disable id FormControl', () => {
        const formGroup = service.createCourseRegistrationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
