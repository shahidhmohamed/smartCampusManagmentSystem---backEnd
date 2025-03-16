import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICourseRegistration } from '../course-registration.model';
import { CourseRegistrationService } from '../service/course-registration.service';

@Component({
  templateUrl: './course-registration-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CourseRegistrationDeleteDialogComponent {
  courseRegistration?: ICourseRegistration;

  protected courseRegistrationService = inject(CourseRegistrationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.courseRegistrationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
