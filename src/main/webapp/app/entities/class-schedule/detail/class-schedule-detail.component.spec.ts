import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ClassScheduleDetailComponent } from './class-schedule-detail.component';

describe('ClassSchedule Management Detail Component', () => {
  let comp: ClassScheduleDetailComponent;
  let fixture: ComponentFixture<ClassScheduleDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClassScheduleDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./class-schedule-detail.component').then(m => m.ClassScheduleDetailComponent),
              resolve: { classSchedule: () => of({ id: '646ee8ed-0b21-4fee-82da-582974bd05ca' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ClassScheduleDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClassScheduleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load classSchedule on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ClassScheduleDetailComponent);

      // THEN
      expect(instance.classSchedule()).toEqual(expect.objectContaining({ id: '646ee8ed-0b21-4fee-82da-582974bd05ca' }));
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
