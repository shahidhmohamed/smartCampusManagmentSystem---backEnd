import { ICourseRegistration, NewCourseRegistration } from './course-registration.model';

export const sampleWithRequiredData: ICourseRegistration = {
  id: '9f0d7ec9-f904-4a37-86ed-5e5f0b3df965',
};

export const sampleWithPartialData: ICourseRegistration = {
  id: 'cd4e5149-b17a-4f8e-afdf-54c73337d9a6',
  studentId: 'round given',
  courseCode: 'both sour lotion',
  duration: 'similar who',
  registrationDate: 'gallery stoop',
};

export const sampleWithFullData: ICourseRegistration = {
  id: '63d5428c-df7a-41df-80cb-54ef3798c538',
  studentId: 'grubby huzzah delightfully',
  courseId: 'for hunt',
  courseCode: 'because assail best',
  duration: 'whenever',
  registrationDate: 'noteworthy',
};

export const sampleWithNewData: NewCourseRegistration = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
