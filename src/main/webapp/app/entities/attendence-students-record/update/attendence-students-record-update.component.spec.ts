import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AttendenceStudentsRecordService } from '../service/attendence-students-record.service';
import { IAttendenceStudentsRecord } from '../attendence-students-record.model';
import { AttendenceStudentsRecordFormService } from './attendence-students-record-form.service';

import { AttendenceStudentsRecordUpdateComponent } from './attendence-students-record-update.component';

describe('AttendenceStudentsRecord Management Update Component', () => {
  let comp: AttendenceStudentsRecordUpdateComponent;
  let fixture: ComponentFixture<AttendenceStudentsRecordUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let attendenceStudentsRecordFormService: AttendenceStudentsRecordFormService;
  let attendenceStudentsRecordService: AttendenceStudentsRecordService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AttendenceStudentsRecordUpdateComponent],
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
      .overrideTemplate(AttendenceStudentsRecordUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AttendenceStudentsRecordUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    attendenceStudentsRecordFormService = TestBed.inject(AttendenceStudentsRecordFormService);
    attendenceStudentsRecordService = TestBed.inject(AttendenceStudentsRecordService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const attendenceStudentsRecord: IAttendenceStudentsRecord = { id: 'a11d264b-8670-4b44-8771-7b26bfb290d9' };

      activatedRoute.data = of({ attendenceStudentsRecord });
      comp.ngOnInit();

      expect(comp.attendenceStudentsRecord).toEqual(attendenceStudentsRecord);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttendenceStudentsRecord>>();
      const attendenceStudentsRecord = { id: '5e4b80ab-1d73-4b99-8c68-ea343bde6562' };
      jest.spyOn(attendenceStudentsRecordFormService, 'getAttendenceStudentsRecord').mockReturnValue(attendenceStudentsRecord);
      jest.spyOn(attendenceStudentsRecordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attendenceStudentsRecord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: attendenceStudentsRecord }));
      saveSubject.complete();

      // THEN
      expect(attendenceStudentsRecordFormService.getAttendenceStudentsRecord).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(attendenceStudentsRecordService.update).toHaveBeenCalledWith(expect.objectContaining(attendenceStudentsRecord));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttendenceStudentsRecord>>();
      const attendenceStudentsRecord = { id: '5e4b80ab-1d73-4b99-8c68-ea343bde6562' };
      jest.spyOn(attendenceStudentsRecordFormService, 'getAttendenceStudentsRecord').mockReturnValue({ id: null });
      jest.spyOn(attendenceStudentsRecordService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attendenceStudentsRecord: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: attendenceStudentsRecord }));
      saveSubject.complete();

      // THEN
      expect(attendenceStudentsRecordFormService.getAttendenceStudentsRecord).toHaveBeenCalled();
      expect(attendenceStudentsRecordService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttendenceStudentsRecord>>();
      const attendenceStudentsRecord = { id: '5e4b80ab-1d73-4b99-8c68-ea343bde6562' };
      jest.spyOn(attendenceStudentsRecordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attendenceStudentsRecord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(attendenceStudentsRecordService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
