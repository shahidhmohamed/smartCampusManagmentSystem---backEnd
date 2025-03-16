import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAssignmentFile } from '../assignment-file.model';
import { AssignmentFileService } from '../service/assignment-file.service';

@Component({
  templateUrl: './assignment-file-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AssignmentFileDeleteDialogComponent {
  assignmentFile?: IAssignmentFile;

  protected assignmentFileService = inject(AssignmentFileService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.assignmentFileService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
