import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import AssignmentFileResolve from './route/assignment-file-routing-resolve.service';

const assignmentFileRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/assignment-file.component').then(m => m.AssignmentFileComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/assignment-file-detail.component').then(m => m.AssignmentFileDetailComponent),
    resolve: {
      assignmentFile: AssignmentFileResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/assignment-file-update.component').then(m => m.AssignmentFileUpdateComponent),
    resolve: {
      assignmentFile: AssignmentFileResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/assignment-file-update.component').then(m => m.AssignmentFileUpdateComponent),
    resolve: {
      assignmentFile: AssignmentFileResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default assignmentFileRoute;
