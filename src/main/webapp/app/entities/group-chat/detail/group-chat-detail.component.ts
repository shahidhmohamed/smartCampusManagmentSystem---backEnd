import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IGroupChat } from '../group-chat.model';

@Component({
  selector: 'jhi-group-chat-detail',
  templateUrl: './group-chat-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class GroupChatDetailComponent {
  groupChat = input<IGroupChat | null>(null);

  previousState(): void {
    window.history.back();
  }
}
