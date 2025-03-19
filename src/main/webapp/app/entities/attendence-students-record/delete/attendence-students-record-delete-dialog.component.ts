import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAttendenceStudentsRecord } from '../attendence-students-record.model';
import { AttendenceStudentsRecordService } from '../service/attendence-students-record.service';

@Component({
  templateUrl: './attendence-students-record-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AttendenceStudentsRecordDeleteDialogComponent {
  attendenceStudentsRecord?: IAttendenceStudentsRecord;

  protected attendenceStudentsRecordService = inject(AttendenceStudentsRecordService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.attendenceStudentsRecordService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
