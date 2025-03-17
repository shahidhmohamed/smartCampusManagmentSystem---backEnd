import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../chat.test-samples';

import { ChatFormService } from './chat-form.service';

describe('Chat Form Service', () => {
  let service: ChatFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChatFormService);
  });

  describe('Service methods', () => {
    describe('createChatFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createChatFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            contactId: expect.any(Object),
            contact: expect.any(Object),
            unreadCount: expect.any(Object),
            muted: expect.any(Object),
            title: expect.any(Object),
            type: expect.any(Object),
            createdAt: expect.any(Object),
            owner: expect.any(Object),
            ownerName: expect.any(Object),
            binaryData: expect.any(Object),
          }),
        );
      });

      it('passing IChat should create a new form with FormGroup', () => {
        const formGroup = service.createChatFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            contactId: expect.any(Object),
            contact: expect.any(Object),
            unreadCount: expect.any(Object),
            muted: expect.any(Object),
            title: expect.any(Object),
            type: expect.any(Object),
            createdAt: expect.any(Object),
            owner: expect.any(Object),
            ownerName: expect.any(Object),
            binaryData: expect.any(Object),
          }),
        );
      });
    });

    describe('getChat', () => {
      it('should return NewChat for default Chat initial value', () => {
        const formGroup = service.createChatFormGroup(sampleWithNewData);

        const chat = service.getChat(formGroup) as any;

        expect(chat).toMatchObject(sampleWithNewData);
      });

      it('should return NewChat for empty Chat initial value', () => {
        const formGroup = service.createChatFormGroup();

        const chat = service.getChat(formGroup) as any;

        expect(chat).toMatchObject({});
      });

      it('should return IChat', () => {
        const formGroup = service.createChatFormGroup(sampleWithRequiredData);

        const chat = service.getChat(formGroup) as any;

        expect(chat).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IChat should not enable id FormControl', () => {
        const formGroup = service.createChatFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewChat should disable id FormControl', () => {
        const formGroup = service.createChatFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
