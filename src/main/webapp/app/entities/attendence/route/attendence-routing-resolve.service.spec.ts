import { TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IAttendence } from '../attendence.model';
import { AttendenceService } from '../service/attendence.service';

import attendenceResolve from './attendence-routing-resolve.service';

describe('Attendence routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: AttendenceService;
  let resultAttendence: IAttendence | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(AttendenceService);
    resultAttendence = undefined;
  });

  describe('resolve', () => {
    it('should return IAttendence returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        attendenceResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultAttendence = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith('ABC');
      expect(resultAttendence).toEqual({ id: 'ABC' });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        attendenceResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultAttendence = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toHaveBeenCalled();
      expect(resultAttendence).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IAttendence>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        attendenceResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultAttendence = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith('ABC');
      expect(resultAttendence).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
