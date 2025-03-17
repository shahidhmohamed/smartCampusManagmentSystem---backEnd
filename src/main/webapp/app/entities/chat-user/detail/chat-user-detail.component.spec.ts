import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ChatUserDetailComponent } from './chat-user-detail.component';

describe('ChatUser Management Detail Component', () => {
  let comp: ChatUserDetailComponent;
  let fixture: ComponentFixture<ChatUserDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChatUserDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./chat-user-detail.component').then(m => m.ChatUserDetailComponent),
              resolve: { chatUser: () => of({ id: 'a95f2d3a-a66c-4c4a-8202-4317d142ff46' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ChatUserDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChatUserDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load chatUser on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ChatUserDetailComponent);

      // THEN
      expect(instance.chatUser()).toEqual(expect.objectContaining({ id: 'a95f2d3a-a66c-4c4a-8202-4317d142ff46' }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
