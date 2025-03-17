import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IChat, NewChat } from '../chat.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IChat for edit and NewChatFormGroupInput for create.
 */
type ChatFormGroupInput = IChat | PartialWithRequiredKeyOf<NewChat>;

type ChatFormDefaults = Pick<NewChat, 'id' | 'muted'>;

type ChatFormGroupContent = {
  id: FormControl<IChat['id'] | NewChat['id']>;
  contactId: FormControl<IChat['contactId']>;
  contact: FormControl<IChat['contact']>;
  unreadCount: FormControl<IChat['unreadCount']>;
  muted: FormControl<IChat['muted']>;
  title: FormControl<IChat['title']>;
  type: FormControl<IChat['type']>;
  createdAt: FormControl<IChat['createdAt']>;
  owner: FormControl<IChat['owner']>;
  ownerName: FormControl<IChat['ownerName']>;
  binaryData: FormControl<IChat['binaryData']>;
};

export type ChatFormGroup = FormGroup<ChatFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ChatFormService {
  createChatFormGroup(chat: ChatFormGroupInput = { id: null }): ChatFormGroup {
    const chatRawValue = {
      ...this.getFormDefaults(),
      ...chat,
    };
    return new FormGroup<ChatFormGroupContent>({
      id: new FormControl(
        { value: chatRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      contactId: new FormControl(chatRawValue.contactId),
      contact: new FormControl(chatRawValue.contact),
      unreadCount: new FormControl(chatRawValue.unreadCount),
      muted: new FormControl(chatRawValue.muted),
      title: new FormControl(chatRawValue.title),
      type: new FormControl(chatRawValue.type),
      createdAt: new FormControl(chatRawValue.createdAt),
      owner: new FormControl(chatRawValue.owner),
      ownerName: new FormControl(chatRawValue.ownerName),
      binaryData: new FormControl(chatRawValue.binaryData),
    });
  }

  getChat(form: ChatFormGroup): IChat | NewChat {
    return form.getRawValue() as IChat | NewChat;
  }

  resetForm(form: ChatFormGroup, chat: ChatFormGroupInput): void {
    const chatRawValue = { ...this.getFormDefaults(), ...chat };
    form.reset(
      {
        ...chatRawValue,
        id: { value: chatRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ChatFormDefaults {
    return {
      id: null,
      muted: false,
    };
  }
}
