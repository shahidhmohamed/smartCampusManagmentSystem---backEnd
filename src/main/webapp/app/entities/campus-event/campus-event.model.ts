import { EventStatus } from 'app/entities/enumerations/event-status.model';

export interface ICampusEvent {
  id: string;
  eventName?: string | null;
  description?: string | null;
  eventDate?: string | null;
  location?: string | null;
  organizerId?: string | null;
  eventType?: string | null;
  capacity?: number | null;
  status?: keyof typeof EventStatus | null;
}

export type NewCampusEvent = Omit<ICampusEvent, 'id'> & { id: null };
