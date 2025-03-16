import { ICampusEvent, NewCampusEvent } from './campus-event.model';

export const sampleWithRequiredData: ICampusEvent = {
  id: '5184e916-a1e9-410c-88ca-b5c2b50fc937',
};

export const sampleWithPartialData: ICampusEvent = {
  id: '437d0283-4ea7-4987-80fc-5bcdaf10220e',
  description: 'curiously whack dreary',
  location: 'nippy solidly',
  organizerId: 'easily hmph',
  capacity: 16617,
  status: 'COMPLETED',
};

export const sampleWithFullData: ICampusEvent = {
  id: 'c9b4e447-1372-484b-a2bc-02582b31238e',
  eventName: 'curiously unaccountably',
  description: 'phew oh finally',
  eventDate: 'serene sardonic gracefully',
  location: 'self-assured',
  organizerId: 'geez freely fooey',
  eventType: 'solidly zowie frankly',
  capacity: 10408,
  status: 'COMPLETED',
};

export const sampleWithNewData: NewCampusEvent = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
