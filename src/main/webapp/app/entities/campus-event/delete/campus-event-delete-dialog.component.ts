import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICampusEvent } from '../campus-event.model';
import { CampusEventService } from '../service/campus-event.service';

@Component({
  templateUrl: './campus-event-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CampusEventDeleteDialogComponent {
  campusEvent?: ICampusEvent;

  protected campusEventService = inject(CampusEventService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.campusEventService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
