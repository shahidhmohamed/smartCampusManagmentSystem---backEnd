import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAssignmentFile, NewAssignmentFile } from '../assignment-file.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAssignmentFile for edit and NewAssignmentFileFormGroupInput for create.
 */
type AssignmentFileFormGroupInput = IAssignmentFile | PartialWithRequiredKeyOf<NewAssignmentFile>;

type AssignmentFileFormDefaults = Pick<NewAssignmentFile, 'id' | 'isSubmitted'>;

type AssignmentFileFormGroupContent = {
  id: FormControl<IAssignmentFile['id'] | NewAssignmentFile['id']>;
  studentId: FormControl<IAssignmentFile['studentId']>;
  assignmentId: FormControl<IAssignmentFile['assignmentId']>;
  name: FormControl<IAssignmentFile['name']>;
  type: FormControl<IAssignmentFile['type']>;
  fileSize: FormControl<IAssignmentFile['fileSize']>;
  createdBy: FormControl<IAssignmentFile['createdBy']>;
  createdAt: FormControl<IAssignmentFile['createdAt']>;
  modifiedAt: FormControl<IAssignmentFile['modifiedAt']>;
  mimeType: FormControl<IAssignmentFile['mimeType']>;
  extension: FormControl<IAssignmentFile['extension']>;
  binaryData: FormControl<IAssignmentFile['binaryData']>;
  binaryDataContentType: FormControl<IAssignmentFile['binaryDataContentType']>;
  markingStatus: FormControl<IAssignmentFile['markingStatus']>;
  grade: FormControl<IAssignmentFile['grade']>;
  feedback: FormControl<IAssignmentFile['feedback']>;
  gradedBy: FormControl<IAssignmentFile['gradedBy']>;
  gradedAt: FormControl<IAssignmentFile['gradedAt']>;
  isSubmitted: FormControl<IAssignmentFile['isSubmitted']>;
};

export type AssignmentFileFormGroup = FormGroup<AssignmentFileFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AssignmentFileFormService {
  createAssignmentFileFormGroup(assignmentFile: AssignmentFileFormGroupInput = { id: null }): AssignmentFileFormGroup {
    const assignmentFileRawValue = {
      ...this.getFormDefaults(),
      ...assignmentFile,
    };
    return new FormGroup<AssignmentFileFormGroupContent>({
      id: new FormControl(
        { value: assignmentFileRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      studentId: new FormControl(assignmentFileRawValue.studentId),
      assignmentId: new FormControl(assignmentFileRawValue.assignmentId),
      name: new FormControl(assignmentFileRawValue.name),
      type: new FormControl(assignmentFileRawValue.type),
      fileSize: new FormControl(assignmentFileRawValue.fileSize),
      createdBy: new FormControl(assignmentFileRawValue.createdBy),
      createdAt: new FormControl(assignmentFileRawValue.createdAt),
      modifiedAt: new FormControl(assignmentFileRawValue.modifiedAt),
      mimeType: new FormControl(assignmentFileRawValue.mimeType),
      extension: new FormControl(assignmentFileRawValue.extension),
      binaryData: new FormControl(assignmentFileRawValue.binaryData),
      binaryDataContentType: new FormControl(assignmentFileRawValue.binaryDataContentType),
      markingStatus: new FormControl(assignmentFileRawValue.markingStatus),
      grade: new FormControl(assignmentFileRawValue.grade),
      feedback: new FormControl(assignmentFileRawValue.feedback),
      gradedBy: new FormControl(assignmentFileRawValue.gradedBy),
      gradedAt: new FormControl(assignmentFileRawValue.gradedAt),
      isSubmitted: new FormControl(assignmentFileRawValue.isSubmitted),
    });
  }

  getAssignmentFile(form: AssignmentFileFormGroup): IAssignmentFile | NewAssignmentFile {
    return form.getRawValue() as IAssignmentFile | NewAssignmentFile;
  }

  resetForm(form: AssignmentFileFormGroup, assignmentFile: AssignmentFileFormGroupInput): void {
    const assignmentFileRawValue = { ...this.getFormDefaults(), ...assignmentFile };
    form.reset(
      {
        ...assignmentFileRawValue,
        id: { value: assignmentFileRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AssignmentFileFormDefaults {
    return {
      id: null,
      isSubmitted: false,
    };
  }
}
