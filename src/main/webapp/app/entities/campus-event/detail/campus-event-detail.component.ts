import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICampusEvent } from '../campus-event.model';

@Component({
  selector: 'jhi-campus-event-detail',
  templateUrl: './campus-event-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CampusEventDetailComponent {
  campusEvent = input<ICampusEvent | null>(null);

  previousState(): void {
    window.history.back();
  }
}
