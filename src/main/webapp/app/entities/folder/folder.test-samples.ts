import { IFolder, NewFolder } from './folder.model';

export const sampleWithRequiredData: IFolder = {
  id: '68f12cc3-4acd-4d50-b1a0-65ffcfdf5ad0',
};

export const sampleWithPartialData: IFolder = {
  id: '8b41f370-4e61-4444-8f4b-a8c4642cdba8',
  name: 'toothpick reiterate naturally',
  contents: 'madly ugh separately',
  course: 'wholly annual',
  createdAt: 'leap',
};

export const sampleWithFullData: IFolder = {
  id: '57db9fd7-fc1b-45a2-92e6-68f09fd95e4f',
  name: 'amid',
  contents: 'against barracks furthermore',
  courseId: 'riser unless',
  course: 'readily',
  semester: 'aha',
  createdBy: 'powerfully',
  createdAt: 'aha meh spice',
  modifiedAt: 'foot cleaner',
  parentId: 'whenever emulsify black',
};

export const sampleWithNewData: NewFolder = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
