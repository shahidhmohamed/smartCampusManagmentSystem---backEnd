import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { GroupChatService } from '../service/group-chat.service';
import { IGroupChat } from '../group-chat.model';
import { GroupChatFormService } from './group-chat-form.service';

import { GroupChatUpdateComponent } from './group-chat-update.component';

describe('GroupChat Management Update Component', () => {
  let comp: GroupChatUpdateComponent;
  let fixture: ComponentFixture<GroupChatUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let groupChatFormService: GroupChatFormService;
  let groupChatService: GroupChatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [GroupChatUpdateComponent],
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
      .overrideTemplate(GroupChatUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GroupChatUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    groupChatFormService = TestBed.inject(GroupChatFormService);
    groupChatService = TestBed.inject(GroupChatService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const groupChat: IGroupChat = { id: '189f68d8-ec01-4d1c-b069-3341ed362611' };

      activatedRoute.data = of({ groupChat });
      comp.ngOnInit();

      expect(comp.groupChat).toEqual(groupChat);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGroupChat>>();
      const groupChat = { id: 'd98dd870-5fd4-41aa-9c45-df0ecff4ffb9' };
      jest.spyOn(groupChatFormService, 'getGroupChat').mockReturnValue(groupChat);
      jest.spyOn(groupChatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ groupChat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: groupChat }));
      saveSubject.complete();

      // THEN
      expect(groupChatFormService.getGroupChat).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(groupChatService.update).toHaveBeenCalledWith(expect.objectContaining(groupChat));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGroupChat>>();
      const groupChat = { id: 'd98dd870-5fd4-41aa-9c45-df0ecff4ffb9' };
      jest.spyOn(groupChatFormService, 'getGroupChat').mockReturnValue({ id: null });
      jest.spyOn(groupChatService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ groupChat: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: groupChat }));
      saveSubject.complete();

      // THEN
      expect(groupChatFormService.getGroupChat).toHaveBeenCalled();
      expect(groupChatService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGroupChat>>();
      const groupChat = { id: 'd98dd870-5fd4-41aa-9c45-df0ecff4ffb9' };
      jest.spyOn(groupChatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ groupChat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(groupChatService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
