import { IChatUser, NewChatUser } from './chat-user.model';

export const sampleWithRequiredData: IChatUser = {
  id: '4c6ea329-92e4-4557-a46d-0dbb87338664',
};

export const sampleWithPartialData: IChatUser = {
  id: 'fd309acd-d295-46f7-9385-58c8ba480e5e',
  userId: 'corrupt tarragon successfully',
  avatar: 'easily',
  about: 'alienated provided',
  phoneNumber: 'trustworthy drat',
};

export const sampleWithFullData: IChatUser = {
  id: '238d5050-874a-4166-825f-d5ad50e450cc',
  userId: 'till',
  avatar: 'ack',
  name: 'only coolly',
  about: 'since oxidize',
  title: 'selfish yum pigsty',
  birthday: 'symbolise trust where',
  address: 'yahoo vaguely cap',
  phoneNumber: 'zowie physical chasuble',
};

export const sampleWithNewData: NewChatUser = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
