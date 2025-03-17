import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMessage, NewMessage } from '../message.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMessage for edit and NewMessageFormGroupInput for create.
 */
type MessageFormGroupInput = IMessage | PartialWithRequiredKeyOf<NewMessage>;

type MessageFormDefaults = Pick<NewMessage, 'id'>;

type MessageFormGroupContent = {
  id: FormControl<IMessage['id'] | NewMessage['id']>;
  content: FormControl<IMessage['content']>;
  createdAt: FormControl<IMessage['createdAt']>;
  senderId: FormControl<IMessage['senderId']>;
  contactId: FormControl<IMessage['contactId']>;
  chatId: FormControl<IMessage['chatId']>;
  groupChatId: FormControl<IMessage['groupChatId']>;
  binaryData: FormControl<IMessage['binaryData']>;
  senderName: FormControl<IMessage['senderName']>;
};

export type MessageFormGroup = FormGroup<MessageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MessageFormService {
  createMessageFormGroup(message: MessageFormGroupInput = { id: null }): MessageFormGroup {
    const messageRawValue = {
      ...this.getFormDefaults(),
      ...message,
    };
    return new FormGroup<MessageFormGroupContent>({
      id: new FormControl(
        { value: messageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      content: new FormControl(messageRawValue.content),
      createdAt: new FormControl(messageRawValue.createdAt),
      senderId: new FormControl(messageRawValue.senderId),
      contactId: new FormControl(messageRawValue.contactId),
      chatId: new FormControl(messageRawValue.chatId),
      groupChatId: new FormControl(messageRawValue.groupChatId),
      binaryData: new FormControl(messageRawValue.binaryData),
      senderName: new FormControl(messageRawValue.senderName),
    });
  }

  getMessage(form: MessageFormGroup): IMessage | NewMessage {
    return form.getRawValue() as IMessage | NewMessage;
  }

  resetForm(form: MessageFormGroup, message: MessageFormGroupInput): void {
    const messageRawValue = { ...this.getFormDefaults(), ...message };
    form.reset(
      {
        ...messageRawValue,
        id: { value: messageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MessageFormDefaults {
    return {
      id: null,
    };
  }
}
