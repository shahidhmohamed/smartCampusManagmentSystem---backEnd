import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AttendenceStudentsRecordDetailComponent } from './attendence-students-record-detail.component';

describe('AttendenceStudentsRecord Management Detail Component', () => {
  let comp: AttendenceStudentsRecordDetailComponent;
  let fixture: ComponentFixture<AttendenceStudentsRecordDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AttendenceStudentsRecordDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () =>
                import('./attendence-students-record-detail.component').then(m => m.AttendenceStudentsRecordDetailComponent),
              resolve: { attendenceStudentsRecord: () => of({ id: '5e4b80ab-1d73-4b99-8c68-ea343bde6562' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AttendenceStudentsRecordDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AttendenceStudentsRecordDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load attendenceStudentsRecord on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AttendenceStudentsRecordDetailComponent);

      // THEN
      expect(instance.attendenceStudentsRecord()).toEqual(expect.objectContaining({ id: '5e4b80ab-1d73-4b99-8c68-ea343bde6562' }));
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
