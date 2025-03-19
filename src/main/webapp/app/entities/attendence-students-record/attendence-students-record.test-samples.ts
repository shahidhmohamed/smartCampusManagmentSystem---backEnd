import { IAttendenceStudentsRecord, NewAttendenceStudentsRecord } from './attendence-students-record.model';

export const sampleWithRequiredData: IAttendenceStudentsRecord = {
  id: '6891622a-fec2-49c3-adca-09a7cabda939',
};

export const sampleWithPartialData: IAttendenceStudentsRecord = {
  id: 'ca1778c6-1a0e-49c4-9df5-752750664f91',
  attendenceId: 'fooey citizen',
  studentId: 'consequently',
  isPresent: false,
  createdBy: 'generously terribly wombat',
};

export const sampleWithFullData: IAttendenceStudentsRecord = {
  id: '81f75272-efb4-4942-8d8f-4fe57d0c677c',
  attendenceId: 'before fund because',
  studentId: 'mmm deficient',
  studentName: 'mockingly accentuate grumpy',
  isPresent: false,
  createdAt: 'vacantly',
  createdBy: 'than nicely',
};

export const sampleWithNewData: NewAttendenceStudentsRecord = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
