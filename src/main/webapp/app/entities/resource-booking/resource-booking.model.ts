import { IResource } from 'app/entities/resource/resource.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IResourceBooking {
  id: string;
  userId?: string | null;
  startTime?: string | null;
  endTime?: string | null;
  status?: keyof typeof Status | null;
  reason?: string | null;
  adminComment?: string | null;
  resource?: Pick<IResource, 'id'> | null;
}

export type NewResourceBooking = Omit<IResourceBooking, 'id'> & { id: null };
