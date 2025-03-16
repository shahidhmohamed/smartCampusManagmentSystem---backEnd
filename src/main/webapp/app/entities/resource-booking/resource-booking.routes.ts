import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import ResourceBookingResolve from './route/resource-booking-routing-resolve.service';

const resourceBookingRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/resource-booking.component').then(m => m.ResourceBookingComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/resource-booking-detail.component').then(m => m.ResourceBookingDetailComponent),
    resolve: {
      resourceBooking: ResourceBookingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/resource-booking-update.component').then(m => m.ResourceBookingUpdateComponent),
    resolve: {
      resourceBooking: ResourceBookingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/resource-booking-update.component').then(m => m.ResourceBookingUpdateComponent),
    resolve: {
      resourceBooking: ResourceBookingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default resourceBookingRoute;
