import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAttendenceStudentsRecord } from '../attendence-students-record.model';
import { AttendenceStudentsRecordService } from '../service/attendence-students-record.service';

const attendenceStudentsRecordResolve = (route: ActivatedRouteSnapshot): Observable<null | IAttendenceStudentsRecord> => {
  const id = route.params.id;
  if (id) {
    return inject(AttendenceStudentsRecordService)
      .find(id)
      .pipe(
        mergeMap((attendenceStudentsRecord: HttpResponse<IAttendenceStudentsRecord>) => {
          if (attendenceStudentsRecord.body) {
            return of(attendenceStudentsRecord.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default attendenceStudentsRecordResolve;
