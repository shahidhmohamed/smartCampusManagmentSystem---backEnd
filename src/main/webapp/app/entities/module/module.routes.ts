import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import ModuleResolve from './route/module-routing-resolve.service';

const moduleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/module.component').then(m => m.ModuleComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/module-detail.component').then(m => m.ModuleDetailComponent),
    resolve: {
      module: ModuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/module-update.component').then(m => m.ModuleUpdateComponent),
    resolve: {
      module: ModuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/module-update.component').then(m => m.ModuleUpdateComponent),
    resolve: {
      module: ModuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default moduleRoute;
