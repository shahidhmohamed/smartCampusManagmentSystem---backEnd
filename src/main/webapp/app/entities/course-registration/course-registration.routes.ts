import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import CourseRegistrationResolve from './route/course-registration-routing-resolve.service';

const courseRegistrationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/course-registration.component').then(m => m.CourseRegistrationComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/course-registration-detail.component').then(m => m.CourseRegistrationDetailComponent),
    resolve: {
      courseRegistration: CourseRegistrationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/course-registration-update.component').then(m => m.CourseRegistrationUpdateComponent),
    resolve: {
      courseRegistration: CourseRegistrationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/course-registration-update.component').then(m => m.CourseRegistrationUpdateComponent),
    resolve: {
      courseRegistration: CourseRegistrationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default courseRegistrationRoute;
