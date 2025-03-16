import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IAssignment } from '../assignment.model';

@Component({
  selector: 'jhi-assignment-detail',
  templateUrl: './assignment-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class AssignmentDetailComponent {
  assignment = input<IAssignment | null>(null);

  previousState(): void {
    window.history.back();
  }
}
