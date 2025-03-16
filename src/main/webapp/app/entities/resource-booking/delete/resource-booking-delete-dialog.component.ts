import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IResourceBooking } from '../resource-booking.model';
import { ResourceBookingService } from '../service/resource-booking.service';

@Component({
  templateUrl: './resource-booking-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ResourceBookingDeleteDialogComponent {
  resourceBooking?: IResourceBooking;

  protected resourceBookingService = inject(ResourceBookingService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.resourceBookingService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
