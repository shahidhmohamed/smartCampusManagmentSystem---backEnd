import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAttendence } from '../attendence.model';
import { AttendenceService } from '../service/attendence.service';
import { AttendenceFormGroup, AttendenceFormService } from './attendence-form.service';

@Component({
  selector: 'jhi-attendence-update',
  templateUrl: './attendence-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AttendenceUpdateComponent implements OnInit {
  isSaving = false;
  attendence: IAttendence | null = null;

  protected attendenceService = inject(AttendenceService);
  protected attendenceFormService = inject(AttendenceFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AttendenceFormGroup = this.attendenceFormService.createAttendenceFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attendence }) => {
      this.attendence = attendence;
      if (attendence) {
        this.updateForm(attendence);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attendence = this.attendenceFormService.getAttendence(this.editForm);
    if (attendence.id !== null) {
      this.subscribeToSaveResponse(this.attendenceService.update(attendence));
    } else {
      this.subscribeToSaveResponse(this.attendenceService.create(attendence));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttendence>>): void {
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

  protected updateForm(attendence: IAttendence): void {
    this.attendence = attendence;
    this.attendenceFormService.resetForm(this.editForm, attendence);
  }
}
