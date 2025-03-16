import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IResourceBooking, NewResourceBooking } from '../resource-booking.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IResourceBooking for edit and NewResourceBookingFormGroupInput for create.
 */
type ResourceBookingFormGroupInput = IResourceBooking | PartialWithRequiredKeyOf<NewResourceBooking>;

type ResourceBookingFormDefaults = Pick<NewResourceBooking, 'id'>;

type ResourceBookingFormGroupContent = {
  id: FormControl<IResourceBooking['id'] | NewResourceBooking['id']>;
  userId: FormControl<IResourceBooking['userId']>;
  startTime: FormControl<IResourceBooking['startTime']>;
  endTime: FormControl<IResourceBooking['endTime']>;
  status: FormControl<IResourceBooking['status']>;
  reason: FormControl<IResourceBooking['reason']>;
  adminComment: FormControl<IResourceBooking['adminComment']>;
  resource: FormControl<IResourceBooking['resource']>;
};

export type ResourceBookingFormGroup = FormGroup<ResourceBookingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ResourceBookingFormService {
  createResourceBookingFormGroup(resourceBooking: ResourceBookingFormGroupInput = { id: null }): ResourceBookingFormGroup {
    const resourceBookingRawValue = {
      ...this.getFormDefaults(),
      ...resourceBooking,
    };
    return new FormGroup<ResourceBookingFormGroupContent>({
      id: new FormControl(
        { value: resourceBookingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      userId: new FormControl(resourceBookingRawValue.userId),
      startTime: new FormControl(resourceBookingRawValue.startTime),
      endTime: new FormControl(resourceBookingRawValue.endTime),
      status: new FormControl(resourceBookingRawValue.status),
      reason: new FormControl(resourceBookingRawValue.reason),
      adminComment: new FormControl(resourceBookingRawValue.adminComment),
      resource: new FormControl(resourceBookingRawValue.resource),
    });
  }

  getResourceBooking(form: ResourceBookingFormGroup): IResourceBooking | NewResourceBooking {
    return form.getRawValue() as IResourceBooking | NewResourceBooking;
  }

  resetForm(form: ResourceBookingFormGroup, resourceBooking: ResourceBookingFormGroupInput): void {
    const resourceBookingRawValue = { ...this.getFormDefaults(), ...resourceBooking };
    form.reset(
      {
        ...resourceBookingRawValue,
        id: { value: resourceBookingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ResourceBookingFormDefaults {
    return {
      id: null,
    };
  }
}
