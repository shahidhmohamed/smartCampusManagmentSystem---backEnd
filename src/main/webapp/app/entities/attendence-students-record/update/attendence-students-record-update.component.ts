import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAttendenceStudentsRecord } from '../attendence-students-record.model';
import { AttendenceStudentsRecordService } from '../service/attendence-students-record.service';
import { AttendenceStudentsRecordFormGroup, AttendenceStudentsRecordFormService } from './attendence-students-record-form.service';

@Component({
  selector: 'jhi-attendence-students-record-update',
  templateUrl: './attendence-students-record-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AttendenceStudentsRecordUpdateComponent implements OnInit {
  isSaving = false;
  attendenceStudentsRecord: IAttendenceStudentsRecord | null = null;

  protected attendenceStudentsRecordService = inject(AttendenceStudentsRecordService);
  protected attendenceStudentsRecordFormService = inject(AttendenceStudentsRecordFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AttendenceStudentsRecordFormGroup = this.attendenceStudentsRecordFormService.createAttendenceStudentsRecordFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attendenceStudentsRecord }) => {
      this.attendenceStudentsRecord = attendenceStudentsRecord;
      if (attendenceStudentsRecord) {
        this.updateForm(attendenceStudentsRecord);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attendenceStudentsRecord = this.attendenceStudentsRecordFormService.getAttendenceStudentsRecord(this.editForm);
    if (attendenceStudentsRecord.id !== null) {
      this.subscribeToSaveResponse(this.attendenceStudentsRecordService.update(attendenceStudentsRecord));
    } else {
      this.subscribeToSaveResponse(this.attendenceStudentsRecordService.create(attendenceStudentsRecord));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttendenceStudentsRecord>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(attendenceStudentsRecord: IAttendenceStudentsRecord): void {
    this.attendenceStudentsRecord = attendenceStudentsRecord;
    this.attendenceStudentsRecordFormService.resetForm(this.editForm, attendenceStudentsRecord);
  }
}
