import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IResource } from 'app/entities/resource/resource.model';
import { ResourceService } from 'app/entities/resource/service/resource.service';
import { ResourceBookingService } from '../service/resource-booking.service';
import { IResourceBooking } from '../resource-booking.model';
import { ResourceBookingFormService } from './resource-booking-form.service';

import { ResourceBookingUpdateComponent } from './resource-booking-update.component';

describe('ResourceBooking Management Update Component', () => {
  let comp: ResourceBookingUpdateComponent;
  let fixture: ComponentFixture<ResourceBookingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let resourceBookingFormService: ResourceBookingFormService;
  let resourceBookingService: ResourceBookingService;
  let resourceService: ResourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ResourceBookingUpdateComponent],
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
      .overrideTemplate(ResourceBookingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ResourceBookingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    resourceBookingFormService = TestBed.inject(ResourceBookingFormService);
    resourceBookingService = TestBed.inject(ResourceBookingService);
    resourceService = TestBed.inject(ResourceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Resource query and add missing value', () => {
      const resourceBooking: IResourceBooking = { id: '1fb84424-c8fd-486a-91b9-84fa89437ecf' };
      const resource: IResource = { id: '68629277-5060-42bd-8ce0-72257336c171' };
      resourceBooking.resource = resource;

      const resourceCollection: IResource[] = [{ id: '68629277-5060-42bd-8ce0-72257336c171' }];
      jest.spyOn(resourceService, 'query').mockReturnValue(of(new HttpResponse({ body: resourceCollection })));
      const additionalResources = [resource];
      const expectedCollection: IResource[] = [...additionalResources, ...resourceCollection];
      jest.spyOn(resourceService, 'addResourceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ resourceBooking });
      comp.ngOnInit();

      expect(resourceService.query).toHaveBeenCalled();
      expect(resourceService.addResourceToCollectionIfMissing).toHaveBeenCalledWith(
        resourceCollection,
        ...additionalResources.map(expect.objectContaining),
      );
      expect(comp.resourcesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const resourceBooking: IResourceBooking = { id: '1fb84424-c8fd-486a-91b9-84fa89437ecf' };
      const resource: IResource = { id: '68629277-5060-42bd-8ce0-72257336c171' };
      resourceBooking.resource = resource;

      activatedRoute.data = of({ resourceBooking });
      comp.ngOnInit();

      expect(comp.resourcesSharedCollection).toContainEqual(resource);
      expect(comp.resourceBooking).toEqual(resourceBooking);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResourceBooking>>();
      const resourceBooking = { id: 'a0fde613-22b6-44c4-b803-2fbe442477c2' };
      jest.spyOn(resourceBookingFormService, 'getResourceBooking').mockReturnValue(resourceBooking);
      jest.spyOn(resourceBookingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resourceBooking });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resourceBooking }));
      saveSubject.complete();

      // THEN
      expect(resourceBookingFormService.getResourceBooking).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(resourceBookingService.update).toHaveBeenCalledWith(expect.objectContaining(resourceBooking));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResourceBooking>>();
      const resourceBooking = { id: 'a0fde613-22b6-44c4-b803-2fbe442477c2' };
      jest.spyOn(resourceBookingFormService, 'getResourceBooking').mockReturnValue({ id: null });
      jest.spyOn(resourceBookingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resourceBooking: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resourceBooking }));
      saveSubject.complete();

      // THEN
      expect(resourceBookingFormService.getResourceBooking).toHaveBeenCalled();
      expect(resourceBookingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResourceBooking>>();
      const resourceBooking = { id: 'a0fde613-22b6-44c4-b803-2fbe442477c2' };
      jest.spyOn(resourceBookingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resourceBooking });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(resourceBookingService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareResource', () => {
      it('Should forward to resourceService', () => {
        const entity = { id: '68629277-5060-42bd-8ce0-72257336c171' };
        const entity2 = { id: '2c769352-41ba-42c3-909c-d7b655ed460c' };
        jest.spyOn(resourceService, 'compareResource');
        comp.compareResource(entity, entity2);
        expect(resourceService.compareResource).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
