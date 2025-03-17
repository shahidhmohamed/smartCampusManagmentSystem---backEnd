import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../chat-user.test-samples';

import { ChatUserFormService } from './chat-user-form.service';

describe('ChatUser Form Service', () => {
  let service: ChatUserFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChatUserFormService);
  });

  describe('Service methods', () => {
    describe('createChatUserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createChatUserFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userId: expect.any(Object),
            avatar: expect.any(Object),
            name: expect.any(Object),
            about: expect.any(Object),
            title: expect.any(Object),
            birthday: expect.any(Object),
            address: expect.any(Object),
            phoneNumber: expect.any(Object),
          }),
        );
      });

      it('passing IChatUser should create a new form with FormGroup', () => {
        const formGroup = service.createChatUserFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userId: expect.any(Object),
            avatar: expect.any(Object),
            name: expect.any(Object),
            about: expect.any(Object),
            title: expect.any(Object),
            birthday: expect.any(Object),
            address: expect.any(Object),
            phoneNumber: expect.any(Object),
          }),
        );
      });
    });

    describe('getChatUser', () => {
      it('should return NewChatUser for default ChatUser initial value', () => {
        const formGroup = service.createChatUserFormGroup(sampleWithNewData);

        const chatUser = service.getChatUser(formGroup) as any;

        expect(chatUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewChatUser for empty ChatUser initial value', () => {
        const formGroup = service.createChatUserFormGroup();

        const chatUser = service.getChatUser(formGroup) as any;

        expect(chatUser).toMatchObject({});
      });

      it('should return IChatUser', () => {
        const formGroup = service.createChatUserFormGroup(sampleWithRequiredData);

        const chatUser = service.getChatUser(formGroup) as any;

        expect(chatUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IChatUser should not enable id FormControl', () => {
        const formGroup = service.createChatUserFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewChatUser should disable id FormControl', () => {
        const formGroup = service.createChatUserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
