import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IGroupChatMembers } from '../group-chat-members.model';
import { GroupChatMembersService } from '../service/group-chat-members.service';
import { GroupChatMembersFormGroup, GroupChatMembersFormService } from './group-chat-members-form.service';

@Component({
  selector: 'jhi-group-chat-members-update',
  templateUrl: './group-chat-members-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class GroupChatMembersUpdateComponent implements OnInit {
  isSaving = false;
  groupChatMembers: IGroupChatMembers | null = null;

  protected groupChatMembersService = inject(GroupChatMembersService);
  protected groupChatMembersFormService = inject(GroupChatMembersFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: GroupChatMembersFormGroup = this.groupChatMembersFormService.createGroupChatMembersFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ groupChatMembers }) => {
      this.groupChatMembers = groupChatMembers;
      if (groupChatMembers) {
        this.updateForm(groupChatMembers);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const groupChatMembers = this.groupChatMembersFormService.getGroupChatMembers(this.editForm);
    if (groupChatMembers.id !== null) {
      this.subscribeToSaveResponse(this.groupChatMembersService.update(groupChatMembers));
    } else {
      this.subscribeToSaveResponse(this.groupChatMembersService.create(groupChatMembers));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGroupChatMembers>>): void {
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

  protected updateForm(groupChatMembers: IGroupChatMembers): void {
    this.groupChatMembers = groupChatMembers;
    this.groupChatMembersFormService.resetForm(this.editForm, groupChatMembers);
  }
}
