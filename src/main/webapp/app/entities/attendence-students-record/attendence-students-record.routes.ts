import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import AttendenceStudentsRecordResolve from './route/attendence-students-record-routing-resolve.service';

const attendenceStudentsRecordRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/attendence-students-record.component').then(m => m.AttendenceStudentsRecordComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/attendence-students-record-detail.component').then(m => m.AttendenceStudentsRecordDetailComponent),
    resolve: {
      attendenceStudentsRecord: AttendenceStudentsRecordResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/attendence-students-record-update.component').then(m => m.AttendenceStudentsRecordUpdateComponent),
    resolve: {
      attendenceStudentsRecord: AttendenceStudentsRecordResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/attendence-students-record-update.component').then(m => m.AttendenceStudentsRecordUpdateComponent),
    resolve: {
      attendenceStudentsRecord: AttendenceStudentsRecordResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default attendenceStudentsRecordRoute;
