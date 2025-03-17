import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IGroupChatMembers } from '../group-chat-members.model';
import { GroupChatMembersService } from '../service/group-chat-members.service';

@Component({
  templateUrl: './group-chat-members-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class GroupChatMembersDeleteDialogComponent {
  groupChatMembers?: IGroupChatMembers;

  protected groupChatMembersService = inject(GroupChatMembersService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.groupChatMembersService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
