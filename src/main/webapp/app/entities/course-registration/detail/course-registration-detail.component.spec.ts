import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CourseRegistrationDetailComponent } from './course-registration-detail.component';

describe('CourseRegistration Management Detail Component', () => {
  let comp: CourseRegistrationDetailComponent;
  let fixture: ComponentFixture<CourseRegistrationDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CourseRegistrationDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./course-registration-detail.component').then(m => m.CourseRegistrationDetailComponent),
              resolve: { courseRegistration: () => of({ id: '130050bb-9f78-421e-9bdf-6a43ba0dfc9b' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CourseRegistrationDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CourseRegistrationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load courseRegistration on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CourseRegistrationDetailComponent);

      // THEN
      expect(instance.courseRegistration()).toEqual(expect.objectContaining({ id: '130050bb-9f78-421e-9bdf-6a43ba0dfc9b' }));
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
