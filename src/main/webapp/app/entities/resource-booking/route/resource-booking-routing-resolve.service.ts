import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResourceBooking } from '../resource-booking.model';
import { ResourceBookingService } from '../service/resource-booking.service';

const resourceBookingResolve = (route: ActivatedRouteSnapshot): Observable<null | IResourceBooking> => {
  const id = route.params.id;
  if (id) {
    return inject(ResourceBookingService)
      .find(id)
      .pipe(
        mergeMap((resourceBooking: HttpResponse<IResourceBooking>) => {
          if (resourceBooking.body) {
            return of(resourceBooking.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default resourceBookingResolve;
