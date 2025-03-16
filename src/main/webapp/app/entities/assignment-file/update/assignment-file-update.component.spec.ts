import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AssignmentFileService } from '../service/assignment-file.service';
import { IAssignmentFile } from '../assignment-file.model';
import { AssignmentFileFormService } from './assignment-file-form.service';

import { AssignmentFileUpdateComponent } from './assignment-file-update.component';

describe('AssignmentFile Management Update Component', () => {
  let comp: AssignmentFileUpdateComponent;
  let fixture: ComponentFixture<AssignmentFileUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assignmentFileFormService: AssignmentFileFormService;
  let assignmentFileService: AssignmentFileService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AssignmentFileUpdateComponent],
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
      .overrideTemplate(AssignmentFileUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssignmentFileUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assignmentFileFormService = TestBed.inject(AssignmentFileFormService);
    assignmentFileService = TestBed.inject(AssignmentFileService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const assignmentFile: IAssignmentFile = { id: 'e8a8b31e-7fb4-45d7-8203-0d891d8ace77' };

      activatedRoute.data = of({ assignmentFile });
      comp.ngOnInit();

      expect(comp.assignmentFile).toEqual(assignmentFile);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssignmentFile>>();
      const assignmentFile = { id: '173e7450-d90d-437f-b33a-fb40c3baeb13' };
      jest.spyOn(assignmentFileFormService, 'getAssignmentFile').mockReturnValue(assignmentFile);
      jest.spyOn(assignmentFileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assignmentFile });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assignmentFile }));
      saveSubject.complete();

      // THEN
      expect(assignmentFileFormService.getAssignmentFile).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(assignmentFileService.update).toHaveBeenCalledWith(expect.objectContaining(assignmentFile));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssignmentFile>>();
      const assignmentFile = { id: '173e7450-d90d-437f-b33a-fb40c3baeb13' };
      jest.spyOn(assignmentFileFormService, 'getAssignmentFile').mockReturnValue({ id: null });
      jest.spyOn(assignmentFileService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assignmentFile: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assignmentFile }));
      saveSubject.complete();

      // THEN
      expect(assignmentFileFormService.getAssignmentFile).toHaveBeenCalled();
      expect(assignmentFileService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssignmentFile>>();
      const assignmentFile = { id: '173e7450-d90d-437f-b33a-fb40c3baeb13' };
      jest.spyOn(assignmentFileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assignmentFile });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assignmentFileService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
