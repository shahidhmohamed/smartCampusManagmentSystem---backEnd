export interface IMessage {
  id: string;
  content?: string | null;
  createdAt?: string | null;
  senderId?: string | null;
  contactId?: string | null;
  chatId?: string | null;
  groupChatId?: string | null;
  binaryData?: string | null;
  senderName?: string | null;
}

export type NewMessage = Omit<IMessage, 'id'> & { id: null };
