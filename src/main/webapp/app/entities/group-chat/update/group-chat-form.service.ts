import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IGroupChat, NewGroupChat } from '../group-chat.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGroupChat for edit and NewGroupChatFormGroupInput for create.
 */
type GroupChatFormGroupInput = IGroupChat | PartialWithRequiredKeyOf<NewGroupChat>;

type GroupChatFormDefaults = Pick<NewGroupChat, 'id' | 'muted'>;

type GroupChatFormGroupContent = {
  id: FormControl<IGroupChat['id'] | NewGroupChat['id']>;
  unreadCount: FormControl<IGroupChat['unreadCount']>;
  muted: FormControl<IGroupChat['muted']>;
  title: FormControl<IGroupChat['title']>;
  type: FormControl<IGroupChat['type']>;
  createdAt: FormControl<IGroupChat['createdAt']>;
  owner: FormControl<IGroupChat['owner']>;
  ownerName: FormControl<IGroupChat['ownerName']>;
};

export type GroupChatFormGroup = FormGroup<GroupChatFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GroupChatFormService {
  createGroupChatFormGroup(groupChat: GroupChatFormGroupInput = { id: null }): GroupChatFormGroup {
    const groupChatRawValue = {
      ...this.getFormDefaults(),
      ...groupChat,
    };
    return new FormGroup<GroupChatFormGroupContent>({
      id: new FormControl(
        { value: groupChatRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      unreadCount: new FormControl(groupChatRawValue.unreadCount),
      muted: new FormControl(groupChatRawValue.muted),
      title: new FormControl(groupChatRawValue.title),
      type: new FormControl(groupChatRawValue.type),
      createdAt: new FormControl(groupChatRawValue.createdAt),
      owner: new FormControl(groupChatRawValue.owner),
      ownerName: new FormControl(groupChatRawValue.ownerName),
    });
  }

  getGroupChat(form: GroupChatFormGroup): IGroupChat | NewGroupChat {
    return form.getRawValue() as IGroupChat | NewGroupChat;
  }

  resetForm(form: GroupChatFormGroup, groupChat: GroupChatFormGroupInput): void {
    const groupChatRawValue = { ...this.getFormDefaults(), ...groupChat };
    form.reset(
      {
        ...groupChatRawValue,
        id: { value: groupChatRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): GroupChatFormDefaults {
    return {
      id: null,
      muted: false,
    };
  }
}
