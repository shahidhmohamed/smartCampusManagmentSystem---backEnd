import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IGroupChatMembers, NewGroupChatMembers } from '../group-chat-members.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGroupChatMembers for edit and NewGroupChatMembersFormGroupInput for create.
 */
type GroupChatMembersFormGroupInput = IGroupChatMembers | PartialWithRequiredKeyOf<NewGroupChatMembers>;

type GroupChatMembersFormDefaults = Pick<NewGroupChatMembers, 'id'>;

type GroupChatMembersFormGroupContent = {
  id: FormControl<IGroupChatMembers['id'] | NewGroupChatMembers['id']>;
  groupChatId: FormControl<IGroupChatMembers['groupChatId']>;
  memberName: FormControl<IGroupChatMembers['memberName']>;
  memberUserId: FormControl<IGroupChatMembers['memberUserId']>;
};

export type GroupChatMembersFormGroup = FormGroup<GroupChatMembersFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GroupChatMembersFormService {
  createGroupChatMembersFormGroup(groupChatMembers: GroupChatMembersFormGroupInput = { id: null }): GroupChatMembersFormGroup {
    const groupChatMembersRawValue = {
      ...this.getFormDefaults(),
      ...groupChatMembers,
    };
    return new FormGroup<GroupChatMembersFormGroupContent>({
      id: new FormControl(
        { value: groupChatMembersRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      groupChatId: new FormControl(groupChatMembersRawValue.groupChatId),
      memberName: new FormControl(groupChatMembersRawValue.memberName),
      memberUserId: new FormControl(groupChatMembersRawValue.memberUserId),
    });
  }

  getGroupChatMembers(form: GroupChatMembersFormGroup): IGroupChatMembers | NewGroupChatMembers {
    return form.getRawValue() as IGroupChatMembers | NewGroupChatMembers;
  }

  resetForm(form: GroupChatMembersFormGroup, groupChatMembers: GroupChatMembersFormGroupInput): void {
    const groupChatMembersRawValue = { ...this.getFormDefaults(), ...groupChatMembers };
    form.reset(
      {
        ...groupChatMembersRawValue,
        id: { value: groupChatMembersRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): GroupChatMembersFormDefaults {
    return {
      id: null,
    };
  }
}
