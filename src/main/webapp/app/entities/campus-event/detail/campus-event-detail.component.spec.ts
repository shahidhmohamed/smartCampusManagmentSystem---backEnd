import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CampusEventDetailComponent } from './campus-event-detail.component';

describe('CampusEvent Management Detail Component', () => {
  let comp: CampusEventDetailComponent;
  let fixture: ComponentFixture<CampusEventDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CampusEventDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./campus-event-detail.component').then(m => m.CampusEventDetailComponent),
              resolve: { campusEvent: () => of({ id: 'f86072de-cac8-4708-be49-02b7571657cf' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CampusEventDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CampusEventDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load campusEvent on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CampusEventDetailComponent);

      // THEN
      expect(instance.campusEvent()).toEqual(expect.objectContaining({ id: 'f86072de-cac8-4708-be49-02b7571657cf' }));
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
