import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICampusEvent, NewCampusEvent } from '../campus-event.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICampusEvent for edit and NewCampusEventFormGroupInput for create.
 */
type CampusEventFormGroupInput = ICampusEvent | PartialWithRequiredKeyOf<NewCampusEvent>;

type CampusEventFormDefaults = Pick<NewCampusEvent, 'id'>;

type CampusEventFormGroupContent = {
  id: FormControl<ICampusEvent['id'] | NewCampusEvent['id']>;
  eventName: FormControl<ICampusEvent['eventName']>;
  description: FormControl<ICampusEvent['description']>;
  eventDate: FormControl<ICampusEvent['eventDate']>;
  location: FormControl<ICampusEvent['location']>;
  organizerId: FormControl<ICampusEvent['organizerId']>;
  eventType: FormControl<ICampusEvent['eventType']>;
  capacity: FormControl<ICampusEvent['capacity']>;
  status: FormControl<ICampusEvent['status']>;
};

export type CampusEventFormGroup = FormGroup<CampusEventFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CampusEventFormService {
  createCampusEventFormGroup(campusEvent: CampusEventFormGroupInput = { id: null }): CampusEventFormGroup {
    const campusEventRawValue = {
      ...this.getFormDefaults(),
      ...campusEvent,
    };
    return new FormGroup<CampusEventFormGroupContent>({
      id: new FormControl(
        { value: campusEventRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      eventName: new FormControl(campusEventRawValue.eventName),
      description: new FormControl(campusEventRawValue.description),
      eventDate: new FormControl(campusEventRawValue.eventDate),
      location: new FormControl(campusEventRawValue.location),
      organizerId: new FormControl(campusEventRawValue.organizerId),
      eventType: new FormControl(campusEventRawValue.eventType),
      capacity: new FormControl(campusEventRawValue.capacity),
      status: new FormControl(campusEventRawValue.status),
    });
  }

  getCampusEvent(form: CampusEventFormGroup): ICampusEvent | NewCampusEvent {
    return form.getRawValue() as ICampusEvent | NewCampusEvent;
  }

  resetForm(form: CampusEventFormGroup, campusEvent: CampusEventFormGroupInput): void {
    const campusEventRawValue = { ...this.getFormDefaults(), ...campusEvent };
    form.reset(
      {
        ...campusEventRawValue,
        id: { value: campusEventRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CampusEventFormDefaults {
    return {
      id: null,
    };
  }
}
