export interface IChat {
  id: string;
  contactId?: string | null;
  contact?: string | null;
  unreadCount?: string | null;
  muted?: boolean | null;
  title?: string | null;
  type?: string | null;
  createdAt?: string | null;
  owner?: string | null;
  ownerName?: string | null;
  binaryData?: string | null;
}

export type NewChat = Omit<IChat, 'id'> & { id: null };
