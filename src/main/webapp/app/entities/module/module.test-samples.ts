import { IModule, NewModule } from './module.model';

export const sampleWithRequiredData: IModule = {
  id: '4bb4b13a-73b6-46bb-a19a-8c81bc1c21ca',
};

export const sampleWithPartialData: IModule = {
  id: 'd7640d69-40f0-4cb8-9df9-d3fdb4d221e0',
  moduleCode: 'psst unexpectedly righteously',
  courseId: 'wonderfully doubtfully',
  semester: 'experienced',
  duration: 'till',
};

export const sampleWithFullData: IModule = {
  id: '0439b340-bdc1-466c-84ad-934b3a928536',
  moduleName: 'longingly',
  moduleCode: 'around after',
  courseId: 'complicated inasmuch',
  semester: 'fumigate rewrite lox',
  lecturerId: 'chatter until',
  duration: 'aha than besides',
};

export const sampleWithNewData: NewModule = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
