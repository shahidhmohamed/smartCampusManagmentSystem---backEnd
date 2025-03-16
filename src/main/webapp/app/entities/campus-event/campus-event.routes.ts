import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import CampusEventResolve from './route/campus-event-routing-resolve.service';

const campusEventRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/campus-event.component').then(m => m.CampusEventComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/campus-event-detail.component').then(m => m.CampusEventDetailComponent),
    resolve: {
      campusEvent: CampusEventResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/campus-event-update.component').then(m => m.CampusEventUpdateComponent),
    resolve: {
      campusEvent: CampusEventResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/campus-event-update.component').then(m => m.CampusEventUpdateComponent),
    resolve: {
      campusEvent: CampusEventResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default campusEventRoute;
