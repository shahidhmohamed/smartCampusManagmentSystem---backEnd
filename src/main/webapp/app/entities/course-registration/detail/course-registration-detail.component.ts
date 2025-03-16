import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICourseRegistration } from '../course-registration.model';

@Component({
  selector: 'jhi-course-registration-detail',
  templateUrl: './course-registration-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CourseRegistrationDetailComponent {
  courseRegistration = input<ICourseRegistration | null>(null);

  previousState(): void {
    window.history.back();
  }
}
