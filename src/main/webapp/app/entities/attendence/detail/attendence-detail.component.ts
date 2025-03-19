import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IAttendence } from '../attendence.model';

@Component({
  selector: 'jhi-attendence-detail',
  templateUrl: './attendence-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class AttendenceDetailComponent {
  attendence = input<IAttendence | null>(null);

  previousState(): void {
    window.history.back();
  }
}
