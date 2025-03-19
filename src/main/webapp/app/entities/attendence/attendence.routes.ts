import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import AttendenceResolve from './route/attendence-routing-resolve.service';

const attendenceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/attendence.component').then(m => m.AttendenceComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/attendence-detail.component').then(m => m.AttendenceDetailComponent),
    resolve: {
      attendence: AttendenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/attendence-update.component').then(m => m.AttendenceUpdateComponent),
    resolve: {
      attendence: AttendenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/attendence-update.component').then(m => m.AttendenceUpdateComponent),
    resolve: {
      attendence: AttendenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default attendenceRoute;
