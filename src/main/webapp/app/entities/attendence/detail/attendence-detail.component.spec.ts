import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AttendenceDetailComponent } from './attendence-detail.component';

describe('Attendence Management Detail Component', () => {
  let comp: AttendenceDetailComponent;
  let fixture: ComponentFixture<AttendenceDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AttendenceDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./attendence-detail.component').then(m => m.AttendenceDetailComponent),
              resolve: { attendence: () => of({ id: 'b77fb128-be50-47af-ba71-8ae37eed997d' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AttendenceDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AttendenceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load attendence on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AttendenceDetailComponent);

      // THEN
      expect(instance.attendence()).toEqual(expect.objectContaining({ id: 'b77fb128-be50-47af-ba71-8ae37eed997d' }));
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
