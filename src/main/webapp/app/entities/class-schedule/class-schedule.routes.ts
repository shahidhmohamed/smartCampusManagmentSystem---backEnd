import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import ClassScheduleResolve from './route/class-schedule-routing-resolve.service';

const classScheduleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/class-schedule.component').then(m => m.ClassScheduleComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/class-schedule-detail.component').then(m => m.ClassScheduleDetailComponent),
    resolve: {
      classSchedule: ClassScheduleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/class-schedule-update.component').then(m => m.ClassScheduleUpdateComponent),
    resolve: {
      classSchedule: ClassScheduleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/class-schedule-update.component').then(m => m.ClassScheduleUpdateComponent),
    resolve: {
      classSchedule: ClassScheduleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default classScheduleRoute;
