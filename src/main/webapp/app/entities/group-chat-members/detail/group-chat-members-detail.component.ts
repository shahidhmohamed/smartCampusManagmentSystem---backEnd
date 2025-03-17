import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IGroupChatMembers } from '../group-chat-members.model';

@Component({
  selector: 'jhi-group-chat-members-detail',
  templateUrl: './group-chat-members-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class GroupChatMembersDetailComponent {
  groupChatMembers = input<IGroupChatMembers | null>(null);

  previousState(): void {
    window.history.back();
  }
}
