import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAttendence } from '../attendence.model';
import { AttendenceService } from '../service/attendence.service';

@Component({
  templateUrl: './attendence-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AttendenceDeleteDialogComponent {
  attendence?: IAttendence;

  protected attendenceService = inject(AttendenceService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.attendenceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
