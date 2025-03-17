import { IMessage, NewMessage } from './message.model';

export const sampleWithRequiredData: IMessage = {
  id: '42bfda03-0f8b-4ea3-a0e7-520813ec664a',
};

export const sampleWithPartialData: IMessage = {
  id: '266b0077-fa17-4e60-99fd-960336032f6f',
  content: 'as',
  senderId: 'wilt stormy vol',
  groupChatId: 'loudly juicy into',
  binaryData: 'oof',
  senderName: 'idle certification partial',
};

export const sampleWithFullData: IMessage = {
  id: '1dbe3ed3-49c7-43ac-8029-cff4cc208105',
  content: 'versus zebra minus',
  createdAt: 'disgorge',
  senderId: 'yahoo until',
  contactId: 'nervously hygienic around',
  chatId: 'vamoose uncork minus',
  groupChatId: 'incidentally',
  binaryData: 'ew microblog',
  senderName: 'flash mid notwithstanding',
};

export const sampleWithNewData: NewMessage = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
