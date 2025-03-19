import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AttendenceService } from '../service/attendence.service';
import { IAttendence } from '../attendence.model';
import { AttendenceFormService } from './attendence-form.service';

import { AttendenceUpdateComponent } from './attendence-update.component';

describe('Attendence Management Update Component', () => {
  let comp: AttendenceUpdateComponent;
  let fixture: ComponentFixture<AttendenceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let attendenceFormService: AttendenceFormService;
  let attendenceService: AttendenceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AttendenceUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AttendenceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AttendenceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    attendenceFormService = TestBed.inject(AttendenceFormService);
    attendenceService = TestBed.inject(AttendenceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const attendence: IAttendence = { id: '30dfb41b-929d-44da-8370-63d967128bad' };

      activatedRoute.data = of({ attendence });
      comp.ngOnInit();

      expect(comp.attendence).toEqual(attendence);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttendence>>();
      const attendence = { id: 'b77fb128-be50-47af-ba71-8ae37eed997d' };
      jest.spyOn(attendenceFormService, 'getAttendence').mockReturnValue(attendence);
      jest.spyOn(attendenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attendence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: attendence }));
      saveSubject.complete();

      // THEN
      expect(attendenceFormService.getAttendence).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(attendenceService.update).toHaveBeenCalledWith(expect.objectContaining(attendence));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttendence>>();
      const attendence = { id: 'b77fb128-be50-47af-ba71-8ae37eed997d' };
      jest.spyOn(attendenceFormService, 'getAttendence').mockReturnValue({ id: null });
      jest.spyOn(attendenceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attendence: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: attendence }));
      saveSubject.complete();

      // THEN
      expect(attendenceFormService.getAttendence).toHaveBeenCalled();
      expect(attendenceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttendence>>();
      const attendence = { id: 'b77fb128-be50-47af-ba71-8ae37eed997d' };
      jest.spyOn(attendenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attendence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(attendenceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
