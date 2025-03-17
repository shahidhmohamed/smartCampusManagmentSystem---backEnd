import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { GroupChatMembersDetailComponent } from './group-chat-members-detail.component';

describe('GroupChatMembers Management Detail Component', () => {
  let comp: GroupChatMembersDetailComponent;
  let fixture: ComponentFixture<GroupChatMembersDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GroupChatMembersDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./group-chat-members-detail.component').then(m => m.GroupChatMembersDetailComponent),
              resolve: { groupChatMembers: () => of({ id: '8c7e1724-b952-4fe7-adc1-714c57bba112' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(GroupChatMembersDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupChatMembersDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load groupChatMembers on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', GroupChatMembersDetailComponent);

      // THEN
      expect(instance.groupChatMembers()).toEqual(expect.objectContaining({ id: '8c7e1724-b952-4fe7-adc1-714c57bba112' }));
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
