import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IChatUser } from '../chat-user.model';
import { ChatUserService } from '../service/chat-user.service';
import { ChatUserFormGroup, ChatUserFormService } from './chat-user-form.service';

@Component({
  selector: 'jhi-chat-user-update',
  templateUrl: './chat-user-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ChatUserUpdateComponent implements OnInit {
  isSaving = false;
  chatUser: IChatUser | null = null;

  protected chatUserService = inject(ChatUserService);
  protected chatUserFormService = inject(ChatUserFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ChatUserFormGroup = this.chatUserFormService.createChatUserFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chatUser }) => {
      this.chatUser = chatUser;
      if (chatUser) {
        this.updateForm(chatUser);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const chatUser = this.chatUserFormService.getChatUser(this.editForm);
    if (chatUser.id !== null) {
      this.subscribeToSaveResponse(this.chatUserService.update(chatUser));
    } else {
      this.subscribeToSaveResponse(this.chatUserService.create(chatUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChatUser>>): void {
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

  protected updateForm(chatUser: IChatUser): void {
    this.chatUser = chatUser;
    this.chatUserFormService.resetForm(this.editForm, chatUser);
  }
}
