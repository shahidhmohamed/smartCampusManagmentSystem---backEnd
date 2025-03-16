import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IClassSchedule } from '../class-schedule.model';
import { ClassScheduleService } from '../service/class-schedule.service';
import { ClassScheduleFormGroup, ClassScheduleFormService } from './class-schedule-form.service';

@Component({
  selector: 'jhi-class-schedule-update',
  templateUrl: './class-schedule-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ClassScheduleUpdateComponent implements OnInit {
  isSaving = false;
  classSchedule: IClassSchedule | null = null;

  protected classScheduleService = inject(ClassScheduleService);
  protected classScheduleFormService = inject(ClassScheduleFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ClassScheduleFormGroup = this.classScheduleFormService.createClassScheduleFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classSchedule }) => {
      this.classSchedule = classSchedule;
      if (classSchedule) {
        this.updateForm(classSchedule);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classSchedule = this.classScheduleFormService.getClassSchedule(this.editForm);
    if (classSchedule.id !== null) {
      this.subscribeToSaveResponse(this.classScheduleService.update(classSchedule));
    } else {
      this.subscribeToSaveResponse(this.classScheduleService.create(classSchedule));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassSchedule>>): void {
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

  protected updateForm(classSchedule: IClassSchedule): void {
    this.classSchedule = classSchedule;
    this.classScheduleFormService.resetForm(this.editForm, classSchedule);
  }
}
