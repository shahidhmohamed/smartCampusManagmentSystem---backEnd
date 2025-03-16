import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ClassScheduleService } from '../service/class-schedule.service';
import { IClassSchedule } from '../class-schedule.model';
import { ClassScheduleFormService } from './class-schedule-form.service';

import { ClassScheduleUpdateComponent } from './class-schedule-update.component';

describe('ClassSchedule Management Update Component', () => {
  let comp: ClassScheduleUpdateComponent;
  let fixture: ComponentFixture<ClassScheduleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let classScheduleFormService: ClassScheduleFormService;
  let classScheduleService: ClassScheduleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ClassScheduleUpdateComponent],
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
      .overrideTemplate(ClassScheduleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClassScheduleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    classScheduleFormService = TestBed.inject(ClassScheduleFormService);
    classScheduleService = TestBed.inject(ClassScheduleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const classSchedule: IClassSchedule = { id: '720fb6fc-3aa8-4c88-8cdc-4bed5d34e880' };

      activatedRoute.data = of({ classSchedule });
      comp.ngOnInit();

      expect(comp.classSchedule).toEqual(classSchedule);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClassSchedule>>();
      const classSchedule = { id: '646ee8ed-0b21-4fee-82da-582974bd05ca' };
      jest.spyOn(classScheduleFormService, 'getClassSchedule').mockReturnValue(classSchedule);
      jest.spyOn(classScheduleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classSchedule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classSchedule }));
      saveSubject.complete();

      // THEN
      expect(classScheduleFormService.getClassSchedule).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(classScheduleService.update).toHaveBeenCalledWith(expect.objectContaining(classSchedule));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClassSchedule>>();
      const classSchedule = { id: '646ee8ed-0b21-4fee-82da-582974bd05ca' };
      jest.spyOn(classScheduleFormService, 'getClassSchedule').mockReturnValue({ id: null });
      jest.spyOn(classScheduleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classSchedule: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classSchedule }));
      saveSubject.complete();

      // THEN
      expect(classScheduleFormService.getClassSchedule).toHaveBeenCalled();
      expect(classScheduleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClassSchedule>>();
      const classSchedule = { id: '646ee8ed-0b21-4fee-82da-582974bd05ca' };
      jest.spyOn(classScheduleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classSchedule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(classScheduleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
