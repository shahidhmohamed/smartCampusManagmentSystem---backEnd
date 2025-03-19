import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAttendenceStudentsRecord, NewAttendenceStudentsRecord } from '../attendence-students-record.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAttendenceStudentsRecord for edit and NewAttendenceStudentsRecordFormGroupInput for create.
 */
type AttendenceStudentsRecordFormGroupInput = IAttendenceStudentsRecord | PartialWithRequiredKeyOf<NewAttendenceStudentsRecord>;

type AttendenceStudentsRecordFormDefaults = Pick<NewAttendenceStudentsRecord, 'id' | 'isPresent'>;

type AttendenceStudentsRecordFormGroupContent = {
  id: FormControl<IAttendenceStudentsRecord['id'] | NewAttendenceStudentsRecord['id']>;
  attendenceId: FormControl<IAttendenceStudentsRecord['attendenceId']>;
  studentId: FormControl<IAttendenceStudentsRecord['studentId']>;
  studentName: FormControl<IAttendenceStudentsRecord['studentName']>;
  isPresent: FormControl<IAttendenceStudentsRecord['isPresent']>;
  createdAt: FormControl<IAttendenceStudentsRecord['createdAt']>;
  createdBy: FormControl<IAttendenceStudentsRecord['createdBy']>;
};

export type AttendenceStudentsRecordFormGroup = FormGroup<AttendenceStudentsRecordFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AttendenceStudentsRecordFormService {
  createAttendenceStudentsRecordFormGroup(
    attendenceStudentsRecord: AttendenceStudentsRecordFormGroupInput = { id: null },
  ): AttendenceStudentsRecordFormGroup {
    const attendenceStudentsRecordRawValue = {
      ...this.getFormDefaults(),
      ...attendenceStudentsRecord,
    };
    return new FormGroup<AttendenceStudentsRecordFormGroupContent>({
      id: new FormControl(
        { value: attendenceStudentsRecordRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      attendenceId: new FormControl(attendenceStudentsRecordRawValue.attendenceId),
      studentId: new FormControl(attendenceStudentsRecordRawValue.studentId),
      studentName: new FormControl(attendenceStudentsRecordRawValue.studentName),
      isPresent: new FormControl(attendenceStudentsRecordRawValue.isPresent),
      createdAt: new FormControl(attendenceStudentsRecordRawValue.createdAt),
      createdBy: new FormControl(attendenceStudentsRecordRawValue.createdBy),
    });
  }

  getAttendenceStudentsRecord(form: AttendenceStudentsRecordFormGroup): IAttendenceStudentsRecord | NewAttendenceStudentsRecord {
    return form.getRawValue() as IAttendenceStudentsRecord | NewAttendenceStudentsRecord;
  }

  resetForm(form: AttendenceStudentsRecordFormGroup, attendenceStudentsRecord: AttendenceStudentsRecordFormGroupInput): void {
    const attendenceStudentsRecordRawValue = { ...this.getFormDefaults(), ...attendenceStudentsRecord };
    form.reset(
      {
        ...attendenceStudentsRecordRawValue,
        id: { value: attendenceStudentsRecordRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AttendenceStudentsRecordFormDefaults {
    return {
      id: null,
      isPresent: false,
    };
  }
}
