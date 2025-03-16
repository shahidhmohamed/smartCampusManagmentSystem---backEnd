import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { EventStatus } from 'app/entities/enumerations/event-status.model';
import { ICampusEvent } from '../campus-event.model';
import { CampusEventService } from '../service/campus-event.service';
import { CampusEventFormGroup, CampusEventFormService } from './campus-event-form.service';

@Component({
  selector: 'jhi-campus-event-update',
  templateUrl: './campus-event-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CampusEventUpdateComponent implements OnInit {
  isSaving = false;
  campusEvent: ICampusEvent | null = null;
  eventStatusValues = Object.keys(EventStatus);

  protected campusEventService = inject(CampusEventService);
  protected campusEventFormService = inject(CampusEventFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CampusEventFormGroup = this.campusEventFormService.createCampusEventFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ campusEvent }) => {
      this.campusEvent = campusEvent;
      if (campusEvent) {
        this.updateForm(campusEvent);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const campusEvent = this.campusEventFormService.getCampusEvent(this.editForm);
    if (campusEvent.id !== null) {
      this.subscribeToSaveResponse(this.campusEventService.update(campusEvent));
    } else {
      this.subscribeToSaveResponse(this.campusEventService.create(campusEvent));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICampusEvent>>): void {
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

  protected updateForm(campusEvent: ICampusEvent): void {
    this.campusEvent = campusEvent;
    this.campusEventFormService.resetForm(this.editForm, campusEvent);
  }
}
