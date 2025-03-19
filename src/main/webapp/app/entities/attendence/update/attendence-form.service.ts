import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAttendence, NewAttendence } from '../attendence.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAttendence for edit and NewAttendenceFormGroupInput for create.
 */
type AttendenceFormGroupInput = IAttendence | PartialWithRequiredKeyOf<NewAttendence>;

type AttendenceFormDefaults = Pick<NewAttendence, 'id'>;

type AttendenceFormGroupContent = {
  id: FormControl<IAttendence['id'] | NewAttendence['id']>;
  createdAt: FormControl<IAttendence['createdAt']>;
  courseId: FormControl<IAttendence['courseId']>;
  courseName: FormControl<IAttendence['courseName']>;
  instructorId: FormControl<IAttendence['instructorId']>;
  instructorName: FormControl<IAttendence['instructorName']>;
  moduleId: FormControl<IAttendence['moduleId']>;
  moduleName: FormControl<IAttendence['moduleName']>;
};

export type AttendenceFormGroup = FormGroup<AttendenceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AttendenceFormService {
  createAttendenceFormGroup(attendence: AttendenceFormGroupInput = { id: null }): AttendenceFormGroup {
    const attendenceRawValue = {
      ...this.getFormDefaults(),
      ...attendence,
    };
    return new FormGroup<AttendenceFormGroupContent>({
      id: new FormControl(
        { value: attendenceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      createdAt: new FormControl(attendenceRawValue.createdAt),
      courseId: new FormControl(attendenceRawValue.courseId),
      courseName: new FormControl(attendenceRawValue.courseName),
      instructorId: new FormControl(attendenceRawValue.instructorId),
      instructorName: new FormControl(attendenceRawValue.instructorName),
      moduleId: new FormControl(attendenceRawValue.moduleId),
      moduleName: new FormControl(attendenceRawValue.moduleName),
    });
  }

  getAttendence(form: AttendenceFormGroup): IAttendence | NewAttendence {
    return form.getRawValue() as IAttendence | NewAttendence;
  }

  resetForm(form: AttendenceFormGroup, attendence: AttendenceFormGroupInput): void {
    const attendenceRawValue = { ...this.getFormDefaults(), ...attendence };
    form.reset(
      {
        ...attendenceRawValue,
        id: { value: attendenceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AttendenceFormDefaults {
    return {
      id: null,
    };
  }
}
