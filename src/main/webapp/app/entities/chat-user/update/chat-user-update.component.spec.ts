import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ChatUserService } from '../service/chat-user.service';
import { IChatUser } from '../chat-user.model';
import { ChatUserFormService } from './chat-user-form.service';

import { ChatUserUpdateComponent } from './chat-user-update.component';

describe('ChatUser Management Update Component', () => {
  let comp: ChatUserUpdateComponent;
  let fixture: ComponentFixture<ChatUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let chatUserFormService: ChatUserFormService;
  let chatUserService: ChatUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ChatUserUpdateComponent],
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
      .overrideTemplate(ChatUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ChatUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    chatUserFormService = TestBed.inject(ChatUserFormService);
    chatUserService = TestBed.inject(ChatUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const chatUser: IChatUser = { id: '0ee8f995-82cc-47ab-ab9e-3ffc1d6f32f7' };

      activatedRoute.data = of({ chatUser });
      comp.ngOnInit();

      expect(comp.chatUser).toEqual(chatUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChatUser>>();
      const chatUser = { id: 'a95f2d3a-a66c-4c4a-8202-4317d142ff46' };
      jest.spyOn(chatUserFormService, 'getChatUser').mockReturnValue(chatUser);
      jest.spyOn(chatUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chatUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: chatUser }));
      saveSubject.complete();

      // THEN
      expect(chatUserFormService.getChatUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(chatUserService.update).toHaveBeenCalledWith(expect.objectContaining(chatUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChatUser>>();
      const chatUser = { id: 'a95f2d3a-a66c-4c4a-8202-4317d142ff46' };
      jest.spyOn(chatUserFormService, 'getChatUser').mockReturnValue({ id: null });
      jest.spyOn(chatUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chatUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: chatUser }));
      saveSubject.complete();

      // THEN
      expect(chatUserFormService.getChatUser).toHaveBeenCalled();
      expect(chatUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChatUser>>();
      const chatUser = { id: 'a95f2d3a-a66c-4c4a-8202-4317d142ff46' };
      jest.spyOn(chatUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chatUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(chatUserService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
