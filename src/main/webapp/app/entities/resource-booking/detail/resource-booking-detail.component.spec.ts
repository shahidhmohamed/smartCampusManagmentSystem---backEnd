import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ResourceBookingDetailComponent } from './resource-booking-detail.component';

describe('ResourceBooking Management Detail Component', () => {
  let comp: ResourceBookingDetailComponent;
  let fixture: ComponentFixture<ResourceBookingDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResourceBookingDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./resource-booking-detail.component').then(m => m.ResourceBookingDetailComponent),
              resolve: { resourceBooking: () => of({ id: 'a0fde613-22b6-44c4-b803-2fbe442477c2' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ResourceBookingDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ResourceBookingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load resourceBooking on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ResourceBookingDetailComponent);

      // THEN
      expect(instance.resourceBooking()).toEqual(expect.objectContaining({ id: 'a0fde613-22b6-44c4-b803-2fbe442477c2' }));
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
