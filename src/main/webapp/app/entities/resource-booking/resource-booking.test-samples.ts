import { IResourceBooking, NewResourceBooking } from './resource-booking.model';

export const sampleWithRequiredData: IResourceBooking = {
  id: 'f05fcb2b-ce78-4864-8988-2acad1060df8',
};

export const sampleWithPartialData: IResourceBooking = {
  id: 'a22029f7-6c88-4543-a38a-da0b4ac6be9e',
  userId: 'sans yieldingly off',
  endTime: 'zesty cantaloupe',
  status: 'PENDING',
  adminComment: 'towards long',
};

export const sampleWithFullData: IResourceBooking = {
  id: '666458a7-5cd0-40d5-9235-57c55682a7ee',
  userId: 'warmly',
  startTime: 'about yahoo',
  endTime: 'cantaloupe spear euphonium',
  status: 'PENDING',
  reason: 'handful',
  adminComment: 'sedately elastic',
};

export const sampleWithNewData: NewResourceBooking = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
