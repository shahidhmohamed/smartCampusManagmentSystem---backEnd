import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FolderDetailComponent } from './folder-detail.component';

describe('Folder Management Detail Component', () => {
  let comp: FolderDetailComponent;
  let fixture: ComponentFixture<FolderDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FolderDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./folder-detail.component').then(m => m.FolderDetailComponent),
              resolve: { folder: () => of({ id: '281cbc29-4129-49fe-84b4-1bae60856bac' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FolderDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FolderDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load folder on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FolderDetailComponent);

      // THEN
      expect(instance.folder()).toEqual(expect.objectContaining({ id: '281cbc29-4129-49fe-84b4-1bae60856bac' }));
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
