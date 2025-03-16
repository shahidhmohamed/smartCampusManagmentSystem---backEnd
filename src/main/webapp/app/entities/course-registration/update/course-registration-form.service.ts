import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICourseRegistration, NewCourseRegistration } from '../course-registration.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICourseRegistration for edit and NewCourseRegistrationFormGroupInput for create.
 */
type CourseRegistrationFormGroupInput = ICourseRegistration | PartialWithRequiredKeyOf<NewCourseRegistration>;

type CourseRegistrationFormDefaults = Pick<NewCourseRegistration, 'id'>;

type CourseRegistrationFormGroupContent = {
  id: FormControl<ICourseRegistration['id'] | NewCourseRegistration['id']>;
  studentId: FormControl<ICourseRegistration['studentId']>;
  courseId: FormControl<ICourseRegistration['courseId']>;
  courseCode: FormControl<ICourseRegistration['courseCode']>;
  duration: FormControl<ICourseRegistration['duration']>;
  registrationDate: FormControl<ICourseRegistration['registrationDate']>;
};

export type CourseRegistrationFormGroup = FormGroup<CourseRegistrationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CourseRegistrationFormService {
  createCourseRegistrationFormGroup(courseRegistration: CourseRegistrationFormGroupInput = { id: null }): CourseRegistrationFormGroup {
    const courseRegistrationRawValue = {
      ...this.getFormDefaults(),
      ...courseRegistration,
    };
    return new FormGroup<CourseRegistrationFormGroupContent>({
      id: new FormControl(
        { value: courseRegistrationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      studentId: new FormControl(courseRegistrationRawValue.studentId),
      courseId: new FormControl(courseRegistrationRawValue.courseId),
      courseCode: new FormControl(courseRegistrationRawValue.courseCode),
      duration: new FormControl(courseRegistrationRawValue.duration),
      registrationDate: new FormControl(courseRegistrationRawValue.registrationDate),
    });
  }

  getCourseRegistration(form: CourseRegistrationFormGroup): ICourseRegistration | NewCourseRegistration {
    return form.getRawValue() as ICourseRegistration | NewCourseRegistration;
  }

  resetForm(form: CourseRegistrationFormGroup, courseRegistration: CourseRegistrationFormGroupInput): void {
    const courseRegistrationRawValue = { ...this.getFormDefaults(), ...courseRegistration };
    form.reset(
      {
        ...courseRegistrationRawValue,
        id: { value: courseRegistrationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CourseRegistrationFormDefaults {
    return {
      id: null,
    };
  }
}
