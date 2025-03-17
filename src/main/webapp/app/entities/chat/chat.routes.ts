import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import ChatResolve from './route/chat-routing-resolve.service';

const chatRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/chat.component').then(m => m.ChatComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/chat-detail.component').then(m => m.ChatDetailComponent),
    resolve: {
      chat: ChatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/chat-update.component').then(m => m.ChatUpdateComponent),
    resolve: {
      chat: ChatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/chat-update.component').then(m => m.ChatUpdateComponent),
    resolve: {
      chat: ChatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default chatRoute;
