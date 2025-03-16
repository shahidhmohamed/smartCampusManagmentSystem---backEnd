import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IClassSchedule, NewClassSchedule } from '../class-schedule.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IClassSchedule for edit and NewClassScheduleFormGroupInput for create.
 */
type ClassScheduleFormGroupInput = IClassSchedule | PartialWithRequiredKeyOf<NewClassSchedule>;

type ClassScheduleFormDefaults = Pick<NewClassSchedule, 'id'>;

type ClassScheduleFormGroupContent = {
  id: FormControl<IClassSchedule['id'] | NewClassSchedule['id']>;
  courseId: FormControl<IClassSchedule['courseId']>;
  moduleId: FormControl<IClassSchedule['moduleId']>;
  instructorId: FormControl<IClassSchedule['instructorId']>;
  scheduleDate: FormControl<IClassSchedule['scheduleDate']>;
  scheduleTimeFrom: FormControl<IClassSchedule['scheduleTimeFrom']>;
  scheduleTimeTo: FormControl<IClassSchedule['scheduleTimeTo']>;
  location: FormControl<IClassSchedule['location']>;
};

export type ClassScheduleFormGroup = FormGroup<ClassScheduleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClassScheduleFormService {
  createClassScheduleFormGroup(classSchedule: ClassScheduleFormGroupInput = { id: null }): ClassScheduleFormGroup {
    const classScheduleRawValue = {
      ...this.getFormDefaults(),
      ...classSchedule,
    };
    return new FormGroup<ClassScheduleFormGroupContent>({
      id: new FormControl(
        { value: classScheduleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      courseId: new FormControl(classScheduleRawValue.courseId),
      moduleId: new FormControl(classScheduleRawValue.moduleId),
      instructorId: new FormControl(classScheduleRawValue.instructorId),
      scheduleDate: new FormControl(classScheduleRawValue.scheduleDate),
      scheduleTimeFrom: new FormControl(classScheduleRawValue.scheduleTimeFrom),
      scheduleTimeTo: new FormControl(classScheduleRawValue.scheduleTimeTo),
      location: new FormControl(classScheduleRawValue.location),
    });
  }

  getClassSchedule(form: ClassScheduleFormGroup): IClassSchedule | NewClassSchedule {
    return form.getRawValue() as IClassSchedule | NewClassSchedule;
  }

  resetForm(form: ClassScheduleFormGroup, classSchedule: ClassScheduleFormGroupInput): void {
    const classScheduleRawValue = { ...this.getFormDefaults(), ...classSchedule };
    form.reset(
      {
        ...classScheduleRawValue,
        id: { value: classScheduleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ClassScheduleFormDefaults {
    return {
      id: null,
    };
  }
}
