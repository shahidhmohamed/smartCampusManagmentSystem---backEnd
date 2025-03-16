import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssignmentFile } from '../assignment-file.model';
import { AssignmentFileService } from '../service/assignment-file.service';

const assignmentFileResolve = (route: ActivatedRouteSnapshot): Observable<null | IAssignmentFile> => {
  const id = route.params.id;
  if (id) {
    return inject(AssignmentFileService)
      .find(id)
      .pipe(
        mergeMap((assignmentFile: HttpResponse<IAssignmentFile>) => {
          if (assignmentFile.body) {
            return of(assignmentFile.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default assignmentFileResolve;
