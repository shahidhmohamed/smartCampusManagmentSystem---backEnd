import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../group-chat-members.test-samples';

import { GroupChatMembersFormService } from './group-chat-members-form.service';

describe('GroupChatMembers Form Service', () => {
  let service: GroupChatMembersFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GroupChatMembersFormService);
  });

  describe('Service methods', () => {
    describe('createGroupChatMembersFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGroupChatMembersFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            groupChatId: expect.any(Object),
            memberName: expect.any(Object),
            memberUserId: expect.any(Object),
          }),
        );
      });

      it('passing IGroupChatMembers should create a new form with FormGroup', () => {
        const formGroup = service.createGroupChatMembersFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            groupChatId: expect.any(Object),
            memberName: expect.any(Object),
            memberUserId: expect.any(Object),
          }),
        );
      });
    });

    describe('getGroupChatMembers', () => {
      it('should return NewGroupChatMembers for default GroupChatMembers initial value', () => {
        const formGroup = service.createGroupChatMembersFormGroup(sampleWithNewData);

        const groupChatMembers = service.getGroupChatMembers(formGroup) as any;

        expect(groupChatMembers).toMatchObject(sampleWithNewData);
      });

      it('should return NewGroupChatMembers for empty GroupChatMembers initial value', () => {
        const formGroup = service.createGroupChatMembersFormGroup();

        const groupChatMembers = service.getGroupChatMembers(formGroup) as any;

        expect(groupChatMembers).toMatchObject({});
      });

      it('should return IGroupChatMembers', () => {
        const formGroup = service.createGroupChatMembersFormGroup(sampleWithRequiredData);

        const groupChatMembers = service.getGroupChatMembers(formGroup) as any;

        expect(groupChatMembers).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGroupChatMembers should not enable id FormControl', () => {
        const formGroup = service.createGroupChatMembersFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGroupChatMembers should disable id FormControl', () => {
        const formGroup = service.createGroupChatMembersFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
