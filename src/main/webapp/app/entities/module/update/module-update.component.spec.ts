import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ModuleService } from '../service/module.service';
import { IModule } from '../module.model';
import { ModuleFormService } from './module-form.service';

import { ModuleUpdateComponent } from './module-update.component';

describe('Module Management Update Component', () => {
  let comp: ModuleUpdateComponent;
  let fixture: ComponentFixture<ModuleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let moduleFormService: ModuleFormService;
  let moduleService: ModuleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ModuleUpdateComponent],
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
      .overrideTemplate(ModuleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ModuleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    moduleFormService = TestBed.inject(ModuleFormService);
    moduleService = TestBed.inject(ModuleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const module: IModule = { id: '0f2773b6-3afb-40d6-a011-518e1674f28a' };

      activatedRoute.data = of({ module });
      comp.ngOnInit();

      expect(comp.module).toEqual(module);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IModule>>();
      const module = { id: '45d29272-e86d-4bcf-b095-4af54a23f61c' };
      jest.spyOn(moduleFormService, 'getModule').mockReturnValue(module);
      jest.spyOn(moduleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ module });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: module }));
      saveSubject.complete();

      // THEN
      expect(moduleFormService.getModule).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(moduleService.update).toHaveBeenCalledWith(expect.objectContaining(module));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IModule>>();
      const module = { id: '45d29272-e86d-4bcf-b095-4af54a23f61c' };
      jest.spyOn(moduleFormService, 'getModule').mockReturnValue({ id: null });
      jest.spyOn(moduleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ module: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: module }));
      saveSubject.complete();

      // THEN
      expect(moduleFormService.getModule).toHaveBeenCalled();
      expect(moduleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IModule>>();
      const module = { id: '45d29272-e86d-4bcf-b095-4af54a23f61c' };
      jest.spyOn(moduleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ module });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(moduleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
