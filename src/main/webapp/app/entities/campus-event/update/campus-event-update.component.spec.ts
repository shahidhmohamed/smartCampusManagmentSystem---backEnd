import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CampusEventService } from '../service/campus-event.service';
import { ICampusEvent } from '../campus-event.model';
import { CampusEventFormService } from './campus-event-form.service';

import { CampusEventUpdateComponent } from './campus-event-update.component';

describe('CampusEvent Management Update Component', () => {
  let comp: CampusEventUpdateComponent;
  let fixture: ComponentFixture<CampusEventUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let campusEventFormService: CampusEventFormService;
  let campusEventService: CampusEventService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CampusEventUpdateComponent],
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
      .overrideTemplate(CampusEventUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CampusEventUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    campusEventFormService = TestBed.inject(CampusEventFormService);
    campusEventService = TestBed.inject(CampusEventService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const campusEvent: ICampusEvent = { id: '882d3a75-0e6f-481b-a13a-6fc8ceec1003' };

      activatedRoute.data = of({ campusEvent });
      comp.ngOnInit();

      expect(comp.campusEvent).toEqual(campusEvent);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICampusEvent>>();
      const campusEvent = { id: 'f86072de-cac8-4708-be49-02b7571657cf' };
      jest.spyOn(campusEventFormService, 'getCampusEvent').mockReturnValue(campusEvent);
      jest.spyOn(campusEventService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ campusEvent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: campusEvent }));
      saveSubject.complete();

      // THEN
      expect(campusEventFormService.getCampusEvent).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(campusEventService.update).toHaveBeenCalledWith(expect.objectContaining(campusEvent));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICampusEvent>>();
      const campusEvent = { id: 'f86072de-cac8-4708-be49-02b7571657cf' };
      jest.spyOn(campusEventFormService, 'getCampusEvent').mockReturnValue({ id: null });
      jest.spyOn(campusEventService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ campusEvent: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: campusEvent }));
      saveSubject.complete();

      // THEN
      expect(campusEventFormService.getCampusEvent).toHaveBeenCalled();
      expect(campusEventService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICampusEvent>>();
      const campusEvent = { id: 'f86072de-cac8-4708-be49-02b7571657cf' };
      jest.spyOn(campusEventService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ campusEvent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(campusEventService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
