import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IGroupChat } from '../group-chat.model';
import { GroupChatService } from '../service/group-chat.service';
import { GroupChatFormGroup, GroupChatFormService } from './group-chat-form.service';

@Component({
  selector: 'jhi-group-chat-update',
  templateUrl: './group-chat-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class GroupChatUpdateComponent implements OnInit {
  isSaving = false;
  groupChat: IGroupChat | null = null;

  protected groupChatService = inject(GroupChatService);
  protected groupChatFormService = inject(GroupChatFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: GroupChatFormGroup = this.groupChatFormService.createGroupChatFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ groupChat }) => {
      this.groupChat = groupChat;
      if (groupChat) {
        this.updateForm(groupChat);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const groupChat = this.groupChatFormService.getGroupChat(this.editForm);
    if (groupChat.id !== null) {
      this.subscribeToSaveResponse(this.groupChatService.update(groupChat));
    } else {
      this.subscribeToSaveResponse(this.groupChatService.create(groupChat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGroupChat>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(groupChat: IGroupChat): void {
    this.groupChat = groupChat;
    this.groupChatFormService.resetForm(this.editForm, groupChat);
  }
}
