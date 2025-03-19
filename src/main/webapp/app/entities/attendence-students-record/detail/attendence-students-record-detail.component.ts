import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IAttendenceStudentsRecord } from '../attendence-students-record.model';

@Component({
  selector: 'jhi-attendence-students-record-detail',
  templateUrl: './attendence-students-record-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class AttendenceStudentsRecordDetailComponent {
  attendenceStudentsRecord = input<IAttendenceStudentsRecord | null>(null);

  previousState(): void {
    window.history.back();
  }
}
