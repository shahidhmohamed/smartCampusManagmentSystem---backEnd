import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClassSchedule } from '../class-schedule.model';
import { ClassScheduleService } from '../service/class-schedule.service';

const classScheduleResolve = (route: ActivatedRouteSnapshot): Observable<null | IClassSchedule> => {
  const id = route.params.id;
  if (id) {
    return inject(ClassScheduleService)
      .find(id)
      .pipe(
        mergeMap((classSchedule: HttpResponse<IClassSchedule>) => {
          if (classSchedule.body) {
            return of(classSchedule.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default classScheduleResolve;
