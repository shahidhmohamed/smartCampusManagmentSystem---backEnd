import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFolder, NewFolder } from '../folder.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFolder for edit and NewFolderFormGroupInput for create.
 */
type FolderFormGroupInput = IFolder | PartialWithRequiredKeyOf<NewFolder>;

type FolderFormDefaults = Pick<NewFolder, 'id'>;

type FolderFormGroupContent = {
  id: FormControl<IFolder['id'] | NewFolder['id']>;
  name: FormControl<IFolder['name']>;
  contents: FormControl<IFolder['contents']>;
  courseId: FormControl<IFolder['courseId']>;
  course: FormControl<IFolder['course']>;
  semester: FormControl<IFolder['semester']>;
  createdBy: FormControl<IFolder['createdBy']>;
  createdAt: FormControl<IFolder['createdAt']>;
  modifiedAt: FormControl<IFolder['modifiedAt']>;
  parentId: FormControl<IFolder['parentId']>;
};

export type FolderFormGroup = FormGroup<FolderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FolderFormService {
  createFolderFormGroup(folder: FolderFormGroupInput = { id: null }): FolderFormGroup {
    const folderRawValue = {
      ...this.getFormDefaults(),
      ...folder,
    };
    return new FormGroup<FolderFormGroupContent>({
      id: new FormControl(
        { value: folderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(folderRawValue.name),
      contents: new FormControl(folderRawValue.contents),
      courseId: new FormControl(folderRawValue.courseId),
      course: new FormControl(folderRawValue.course),
      semester: new FormControl(folderRawValue.semester),
      createdBy: new FormControl(folderRawValue.createdBy),
      createdAt: new FormControl(folderRawValue.createdAt),
      modifiedAt: new FormControl(folderRawValue.modifiedAt),
      parentId: new FormControl(folderRawValue.parentId),
    });
  }

  getFolder(form: FolderFormGroup): IFolder | NewFolder {
    return form.getRawValue() as IFolder | NewFolder;
  }

  resetForm(form: FolderFormGroup, folder: FolderFormGroupInput): void {
    const folderRawValue = { ...this.getFormDefaults(), ...folder };
    form.reset(
      {
        ...folderRawValue,
        id: { value: folderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FolderFormDefaults {
    return {
      id: null,
    };
  }
}
