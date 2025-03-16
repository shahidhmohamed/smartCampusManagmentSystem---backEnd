import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAssignment, NewAssignment } from '../assignment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAssignment for edit and NewAssignmentFormGroupInput for create.
 */
type AssignmentFormGroupInput = IAssignment | PartialWithRequiredKeyOf<NewAssignment>;

type AssignmentFormDefaults = Pick<NewAssignment, 'id'>;

type AssignmentFormGroupContent = {
  id: FormControl<IAssignment['id'] | NewAssignment['id']>;
  title: FormControl<IAssignment['title']>;
  description: FormControl<IAssignment['description']>;
  courseId: FormControl<IAssignment['courseId']>;
  moduleId: FormControl<IAssignment['moduleId']>;
  instructorId: FormControl<IAssignment['instructorId']>;
  createdBy: FormControl<IAssignment['createdBy']>;
  createdAt: FormControl<IAssignment['createdAt']>;
  modifiedAt: FormControl<IAssignment['modifiedAt']>;
  deadLine: FormControl<IAssignment['deadLine']>;
};

export type AssignmentFormGroup = FormGroup<AssignmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AssignmentFormService {
  createAssignmentFormGroup(assignment: AssignmentFormGroupInput = { id: null }): AssignmentFormGroup {
    const assignmentRawValue = {
      ...this.getFormDefaults(),
      ...assignment,
    };
    return new FormGroup<AssignmentFormGroupContent>({
      id: new FormControl(
        { value: assignmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(assignmentRawValue.title),
      description: new FormControl(assignmentRawValue.description),
      courseId: new FormControl(assignmentRawValue.courseId),
      moduleId: new FormControl(assignmentRawValue.moduleId),
      instructorId: new FormControl(assignmentRawValue.instructorId),
      createdBy: new FormControl(assignmentRawValue.createdBy),
      createdAt: new FormControl(assignmentRawValue.createdAt),
      modifiedAt: new FormControl(assignmentRawValue.modifiedAt),
      deadLine: new FormControl(assignmentRawValue.deadLine),
    });
  }

  getAssignment(form: AssignmentFormGroup): IAssignment | NewAssignment {
    return form.getRawValue() as IAssignment | NewAssignment;
  }

  resetForm(form: AssignmentFormGroup, assignment: AssignmentFormGroupInput): void {
    const assignmentRawValue = { ...this.getFormDefaults(), ...assignment };
    form.reset(
      {
        ...assignmentRawValue,
        id: { value: assignmentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AssignmentFormDefaults {
    return {
      id: null,
    };
  }
}
