import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IGroupChat } from '../group-chat.model';
import { GroupChatService } from '../service/group-chat.service';

@Component({
  templateUrl: './group-chat-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class GroupChatDeleteDialogComponent {
  groupChat?: IGroupChat;

  protected groupChatService = inject(GroupChatService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.groupChatService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
