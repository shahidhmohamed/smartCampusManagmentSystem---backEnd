export interface IAttendenceStudentsRecord {
  id: string;
  attendenceId?: string | null;
  studentId?: string | null;
  studentName?: string | null;
  isPresent?: boolean | null;
  createdAt?: string | null;
  createdBy?: string | null;
}

export type NewAttendenceStudentsRecord = Omit<IAttendenceStudentsRecord, 'id'> & { id: null };
