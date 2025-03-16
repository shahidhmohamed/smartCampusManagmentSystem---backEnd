import { IClassSchedule, NewClassSchedule } from './class-schedule.model';

export const sampleWithRequiredData: IClassSchedule = {
  id: '140743db-f310-417c-96aa-aa09f1a5b5d0',
};

export const sampleWithPartialData: IClassSchedule = {
  id: 'd0cf5827-8921-4c92-8a77-4d64af8e2e14',
  courseId: 'bah utilization',
  moduleId: 'numb',
  instructorId: 'mobility',
  scheduleTimeFrom: 'shell absent',
  scheduleTimeTo: 'print mesh recent',
  location: 'ew',
};

export const sampleWithFullData: IClassSchedule = {
  id: '7e2d5e56-3aec-4f90-a3d7-a7a282a660d0',
  courseId: 'cinch because',
  moduleId: 'seriously',
  instructorId: 'soliloquy',
  scheduleDate: 'horde very if',
  scheduleTimeFrom: 'aboard presell',
  scheduleTimeTo: 'critical',
  location: 'glittering',
};

export const sampleWithNewData: NewClassSchedule = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
