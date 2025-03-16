import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICampusEvent } from '../campus-event.model';
import { CampusEventService } from '../service/campus-event.service';

const campusEventResolve = (route: ActivatedRouteSnapshot): Observable<null | ICampusEvent> => {
  const id = route.params.id;
  if (id) {
    return inject(CampusEventService)
      .find(id)
      .pipe(
        mergeMap((campusEvent: HttpResponse<ICampusEvent>) => {
          if (campusEvent.body) {
            return of(campusEvent.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default campusEventResolve;
