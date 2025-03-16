import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IResource, NewResource } from '../resource.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IResource for edit and NewResourceFormGroupInput for create.
 */
type ResourceFormGroupInput = IResource | PartialWithRequiredKeyOf<NewResource>;

type ResourceFormDefaults = Pick<NewResource, 'id' | 'availability'>;

type ResourceFormGroupContent = {
  id: FormControl<IResource['id'] | NewResource['id']>;
  resourceId: FormControl<IResource['resourceId']>;
  name: FormControl<IResource['name']>;
  resourceType: FormControl<IResource['resourceType']>;
  location: FormControl<IResource['location']>;
  capacity: FormControl<IResource['capacity']>;
  availability: FormControl<IResource['availability']>;
};

export type ResourceFormGroup = FormGroup<ResourceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ResourceFormService {
  createResourceFormGroup(resource: ResourceFormGroupInput = { id: null }): ResourceFormGroup {
    const resourceRawValue = {
      ...this.getFormDefaults(),
      ...resource,
    };
    return new FormGroup<ResourceFormGroupContent>({
      id: new FormControl(
        { value: resourceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      resourceId: new FormControl(resourceRawValue.resourceId),
      name: new FormControl(resourceRawValue.name),
      resourceType: new FormControl(resourceRawValue.resourceType),
      location: new FormControl(resourceRawValue.location),
      capacity: new FormControl(resourceRawValue.capacity),
      availability: new FormControl(resourceRawValue.availability),
    });
  }

  getResource(form: ResourceFormGroup): IResource | NewResource {
    return form.getRawValue() as IResource | NewResource;
  }

  resetForm(form: ResourceFormGroup, resource: ResourceFormGroupInput): void {
    const resourceRawValue = { ...this.getFormDefaults(), ...resource };
    form.reset(
      {
        ...resourceRawValue,
        id: { value: resourceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ResourceFormDefaults {
    return {
      id: null,
      availability: false,
    };
  }
}
