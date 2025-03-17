import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IChatUser, NewChatUser } from '../chat-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IChatUser for edit and NewChatUserFormGroupInput for create.
 */
type ChatUserFormGroupInput = IChatUser | PartialWithRequiredKeyOf<NewChatUser>;

type ChatUserFormDefaults = Pick<NewChatUser, 'id'>;

type ChatUserFormGroupContent = {
  id: FormControl<IChatUser['id'] | NewChatUser['id']>;
  userId: FormControl<IChatUser['userId']>;
  avatar: FormControl<IChatUser['avatar']>;
  name: FormControl<IChatUser['name']>;
  about: FormControl<IChatUser['about']>;
  title: FormControl<IChatUser['title']>;
  birthday: FormControl<IChatUser['birthday']>;
  address: FormControl<IChatUser['address']>;
  phoneNumber: FormControl<IChatUser['phoneNumber']>;
};

export type ChatUserFormGroup = FormGroup<ChatUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ChatUserFormService {
  createChatUserFormGroup(chatUser: ChatUserFormGroupInput = { id: null }): ChatUserFormGroup {
    const chatUserRawValue = {
      ...this.getFormDefaults(),
      ...chatUser,
    };
    return new FormGroup<ChatUserFormGroupContent>({
      id: new FormControl(
        { value: chatUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      userId: new FormControl(chatUserRawValue.userId),
      avatar: new FormControl(chatUserRawValue.avatar),
      name: new FormControl(chatUserRawValue.name),
      about: new FormControl(chatUserRawValue.about),
      title: new FormControl(chatUserRawValue.title),
      birthday: new FormControl(chatUserRawValue.birthday),
      address: new FormControl(chatUserRawValue.address),
      phoneNumber: new FormControl(chatUserRawValue.phoneNumber),
    });
  }

  getChatUser(form: ChatUserFormGroup): IChatUser | NewChatUser {
    return form.getRawValue() as IChatUser | NewChatUser;
  }

  resetForm(form: ChatUserFormGroup, chatUser: ChatUserFormGroupInput): void {
    const chatUserRawValue = { ...this.getFormDefaults(), ...chatUser };
    form.reset(
      {
        ...chatUserRawValue,
        id: { value: chatUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ChatUserFormDefaults {
    return {
      id: null,
    };
  }
}
