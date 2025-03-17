import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IMessage } from '../message.model';

@Component({
  selector: 'jhi-message-detail',
  templateUrl: './message-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class MessageDetailComponent {
  message = input<IMessage | null>(null);

  previousState(): void {
    window.history.back();
  }
}
