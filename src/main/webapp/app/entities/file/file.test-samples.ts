import { IFile, NewFile } from './file.model';

export const sampleWithRequiredData: IFile = {
  id: '4f164679-f77b-4214-90fb-95a6a5828dc5',
  binaryData: '../fake-data/blob/hipster.png',
  binaryDataContentType: 'unknown',
};

export const sampleWithPartialData: IFile = {
  id: 'e858cbe2-fb05-41bf-9bb9-3b0c3c9cf89d',
  folderId: 'ugh queasy zowie',
  type: 'although advertisement which',
  fileSize: 17199,
  mimeType: 'outside',
  extension: 'plugin',
  binaryData: '../fake-data/blob/hipster.png',
  binaryDataContentType: 'unknown',
};

export const sampleWithFullData: IFile = {
  id: '310e3547-cb5e-407f-82ba-3f387e2aa029',
  folderId: 'where',
  name: 'atomize kindly front',
  type: 'fooey draft drat',
  fileSize: 5085,
  createdBy: 'when but knife',
  createdAt: 'upsell into via',
  modifiedAt: 'aftermath',
  mimeType: 'hunt cooperative',
  extension: 'softly',
  binaryData: '../fake-data/blob/hipster.png',
  binaryDataContentType: 'unknown',
};

export const sampleWithNewData: NewFile = {
  binaryData: '../fake-data/blob/hipster.png',
  binaryDataContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
