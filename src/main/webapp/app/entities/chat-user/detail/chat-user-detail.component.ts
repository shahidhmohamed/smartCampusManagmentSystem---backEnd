import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IChatUser } from '../chat-user.model';

@Component({
  selector: 'jhi-chat-user-detail',
  templateUrl: './chat-user-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ChatUserDetailComponent {
  chatUser = input<IChatUser | null>(null);

  previousState(): void {
    window.history.back();
  }
}
