import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IClassSchedule } from '../class-schedule.model';

@Component({
  selector: 'jhi-class-schedule-detail',
  templateUrl: './class-schedule-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ClassScheduleDetailComponent {
  classSchedule = input<IClassSchedule | null>(null);

  previousState(): void {
    window.history.back();
  }
}
