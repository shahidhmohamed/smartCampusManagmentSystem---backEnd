import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICourseRegistration } from '../course-registration.model';
import { CourseRegistrationService } from '../service/course-registration.service';
import { CourseRegistrationFormGroup, CourseRegistrationFormService } from './course-registration-form.service';

@Component({
  selector: 'jhi-course-registration-update',
  templateUrl: './course-registration-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CourseRegistrationUpdateComponent implements OnInit {
  isSaving = false;
  courseRegistration: ICourseRegistration | null = null;

  protected courseRegistrationService = inject(CourseRegistrationService);
  protected courseRegistrationFormService = inject(CourseRegistrationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CourseRegistrationFormGroup = this.courseRegistrationFormService.createCourseRegistrationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseRegistration }) => {
      this.courseRegistration = courseRegistration;
      if (courseRegistration) {
        this.updateForm(courseRegistration);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const courseRegistration = this.courseRegistrationFormService.getCourseRegistration(this.editForm);
    if (courseRegistration.id !== null) {
      this.subscribeToSaveResponse(this.courseRegistrationService.update(courseRegistration));
    } else {
      this.subscribeToSaveResponse(this.courseRegistrationService.create(courseRegistration));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourseRegistration>>): void {
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

  protected updateForm(courseRegistration: ICourseRegistration): void {
    this.courseRegistration = courseRegistration;
    this.courseRegistrationFormService.resetForm(this.editForm, courseRegistration);
  }
}
