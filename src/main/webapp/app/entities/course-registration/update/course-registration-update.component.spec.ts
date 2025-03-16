import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CourseRegistrationService } from '../service/course-registration.service';
import { ICourseRegistration } from '../course-registration.model';
import { CourseRegistrationFormService } from './course-registration-form.service';

import { CourseRegistrationUpdateComponent } from './course-registration-update.component';

describe('CourseRegistration Management Update Component', () => {
  let comp: CourseRegistrationUpdateComponent;
  let fixture: ComponentFixture<CourseRegistrationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let courseRegistrationFormService: CourseRegistrationFormService;
  let courseRegistrationService: CourseRegistrationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CourseRegistrationUpdateComponent],
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
      .overrideTemplate(CourseRegistrationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CourseRegistrationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    courseRegistrationFormService = TestBed.inject(CourseRegistrationFormService);
    courseRegistrationService = TestBed.inject(CourseRegistrationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const courseRegistration: ICourseRegistration = { id: '75889437-bc54-46ad-b827-8fef7afc2983' };

      activatedRoute.data = of({ courseRegistration });
      comp.ngOnInit();

      expect(comp.courseRegistration).toEqual(courseRegistration);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourseRegistration>>();
      const courseRegistration = { id: '130050bb-9f78-421e-9bdf-6a43ba0dfc9b' };
      jest.spyOn(courseRegistrationFormService, 'getCourseRegistration').mockReturnValue(courseRegistration);
      jest.spyOn(courseRegistrationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ courseRegistration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: courseRegistration }));
      saveSubject.complete();

      // THEN
      expect(courseRegistrationFormService.getCourseRegistration).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(courseRegistrationService.update).toHaveBeenCalledWith(expect.objectContaining(courseRegistration));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourseRegistration>>();
      const courseRegistration = { id: '130050bb-9f78-421e-9bdf-6a43ba0dfc9b' };
      jest.spyOn(courseRegistrationFormService, 'getCourseRegistration').mockReturnValue({ id: null });
      jest.spyOn(courseRegistrationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ courseRegistration: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: courseRegistration }));
      saveSubject.complete();

      // THEN
      expect(courseRegistrationFormService.getCourseRegistration).toHaveBeenCalled();
      expect(courseRegistrationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourseRegistration>>();
      const courseRegistration = { id: '130050bb-9f78-421e-9bdf-6a43ba0dfc9b' };
      jest.spyOn(courseRegistrationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ courseRegistration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(courseRegistrationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
