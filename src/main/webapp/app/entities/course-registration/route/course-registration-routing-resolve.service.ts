import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICourseRegistration } from '../course-registration.model';
import { CourseRegistrationService } from '../service/course-registration.service';

const courseRegistrationResolve = (route: ActivatedRouteSnapshot): Observable<null | ICourseRegistration> => {
  const id = route.params.id;
  if (id) {
    return inject(CourseRegistrationService)
      .find(id)
      .pipe(
        mergeMap((courseRegistration: HttpResponse<ICourseRegistration>) => {
          if (courseRegistration.body) {
            return of(courseRegistration.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default courseRegistrationResolve;
