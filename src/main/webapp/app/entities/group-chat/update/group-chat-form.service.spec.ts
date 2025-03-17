import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../group-chat.test-samples';

import { GroupChatFormService } from './group-chat-form.service';

describe('GroupChat Form Service', () => {
  let service: GroupChatFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GroupChatFormService);
  });

  describe('Service methods', () => {
    describe('createGroupChatFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGroupChatFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            unreadCount: expect.any(Object),
            muted: expect.any(Object),
            title: expect.any(Object),
            type: expect.any(Object),
            createdAt: expect.any(Object),
            owner: expect.any(Object),
            ownerName: expect.any(Object),
          }),
        );
      });

      it('passing IGroupChat should create a new form with FormGroup', () => {
        const formGroup = service.createGroupChatFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            unreadCount: expect.any(Object),
            muted: expect.any(Object),
            title: expect.any(Object),
            type: expect.any(Object),
            createdAt: expect.any(Object),
            owner: expect.any(Object),
            ownerName: expect.any(Object),
          }),
        );
      });
    });

    describe('getGroupChat', () => {
      it('should return NewGroupChat for default GroupChat initial value', () => {
        const formGroup = service.createGroupChatFormGroup(sampleWithNewData);

        const groupChat = service.getGroupChat(formGroup) as any;

        expect(groupChat).toMatchObject(sampleWithNewData);
      });

      it('should return NewGroupChat for empty GroupChat initial value', () => {
        const formGroup = service.createGroupChatFormGroup();

        const groupChat = service.getGroupChat(formGroup) as any;

        expect(groupChat).toMatchObject({});
      });

      it('should return IGroupChat', () => {
        const formGroup = service.createGroupChatFormGroup(sampleWithRequiredData);

        const groupChat = service.getGroupChat(formGroup) as any;

        expect(groupChat).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGroupChat should not enable id FormControl', () => {
        const formGroup = service.createGroupChatFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGroupChat should disable id FormControl', () => {
        const formGroup = service.createGroupChatFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
