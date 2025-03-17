import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IChatUser } from '../chat-user.model';
import { ChatUserService } from '../service/chat-user.service';

@Component({
  templateUrl: './chat-user-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ChatUserDeleteDialogComponent {
  chatUser?: IChatUser;

  protected chatUserService = inject(ChatUserService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.chatUserService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
