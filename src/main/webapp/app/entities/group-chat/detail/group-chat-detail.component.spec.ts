import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { GroupChatDetailComponent } from './group-chat-detail.component';

describe('GroupChat Management Detail Component', () => {
  let comp: GroupChatDetailComponent;
  let fixture: ComponentFixture<GroupChatDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GroupChatDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./group-chat-detail.component').then(m => m.GroupChatDetailComponent),
              resolve: { groupChat: () => of({ id: 'd98dd870-5fd4-41aa-9c45-df0ecff4ffb9' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(GroupChatDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupChatDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load groupChat on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', GroupChatDetailComponent);

      // THEN
      expect(instance.groupChat()).toEqual(expect.objectContaining({ id: 'd98dd870-5fd4-41aa-9c45-df0ecff4ffb9' }));
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
