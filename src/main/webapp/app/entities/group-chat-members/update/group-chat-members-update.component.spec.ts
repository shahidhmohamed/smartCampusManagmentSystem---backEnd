import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { GroupChatMembersService } from '../service/group-chat-members.service';
import { IGroupChatMembers } from '../group-chat-members.model';
import { GroupChatMembersFormService } from './group-chat-members-form.service';

import { GroupChatMembersUpdateComponent } from './group-chat-members-update.component';

describe('GroupChatMembers Management Update Component', () => {
  let comp: GroupChatMembersUpdateComponent;
  let fixture: ComponentFixture<GroupChatMembersUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let groupChatMembersFormService: GroupChatMembersFormService;
  let groupChatMembersService: GroupChatMembersService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [GroupChatMembersUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(GroupChatMembersUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GroupChatMembersUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    groupChatMembersFormService = TestBed.inject(GroupChatMembersFormService);
    groupChatMembersService = TestBed.inject(GroupChatMembersService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const groupChatMembers: IGroupChatMembers = { id: 'fb5e832d-e869-4c74-9f52-477805dfaba8' };

      activatedRoute.data = of({ groupChatMembers });
      comp.ngOnInit();

      expect(comp.groupChatMembers).toEqual(groupChatMembers);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGroupChatMembers>>();
      const groupChatMembers = { id: '8c7e1724-b952-4fe7-adc1-714c57bba112' };
      jest.spyOn(groupChatMembersFormService, 'getGroupChatMembers').mockReturnValue(groupChatMembers);
      jest.spyOn(groupChatMembersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ groupChatMembers });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: groupChatMembers }));
      saveSubject.complete();

      // THEN
      expect(groupChatMembersFormService.getGroupChatMembers).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(groupChatMembersService.update).toHaveBeenCalledWith(expect.objectContaining(groupChatMembers));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGroupChatMembers>>();
      const groupChatMembers = { id: '8c7e1724-b952-4fe7-adc1-714c57bba112' };
      jest.spyOn(groupChatMembersFormService, 'getGroupChatMembers').mockReturnValue({ id: null });
      jest.spyOn(groupChatMembersService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ groupChatMembers: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: groupChatMembers }));
      saveSubject.complete();

      // THEN
      expect(groupChatMembersFormService.getGroupChatMembers).toHaveBeenCalled();
      expect(groupChatMembersService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGroupChatMembers>>();
      const groupChatMembers = { id: '8c7e1724-b952-4fe7-adc1-714c57bba112' };
      jest.spyOn(groupChatMembersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ groupChatMembers });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(groupChatMembersService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
