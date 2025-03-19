import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAttendence } from '../attendence.model';
import { AttendenceService } from '../service/attendence.service';

const attendenceResolve = (route: ActivatedRouteSnapshot): Observable<null | IAttendence> => {
  const id = route.params.id;
  if (id) {
    return inject(AttendenceService)
      .find(id)
      .pipe(
        mergeMap((attendence: HttpResponse<IAttendence>) => {
          if (attendence.body) {
            return of(attendence.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default attendenceResolve;
