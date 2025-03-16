import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IResourceBooking } from '../resource-booking.model';

@Component({
  selector: 'jhi-resource-booking-detail',
  templateUrl: './resource-booking-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ResourceBookingDetailComponent {
  resourceBooking = input<IResourceBooking | null>(null);

  previousState(): void {
    window.history.back();
  }
}
