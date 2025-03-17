import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import ChatUserResolve from './route/chat-user-routing-resolve.service';

const chatUserRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/chat-user.component').then(m => m.ChatUserComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/chat-user-detail.component').then(m => m.ChatUserDetailComponent),
    resolve: {
      chatUser: ChatUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/chat-user-update.component').then(m => m.ChatUserUpdateComponent),
    resolve: {
      chatUser: ChatUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/chat-user-update.component').then(m => m.ChatUserUpdateComponent),
    resolve: {
      chatUser: ChatUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default chatUserRoute;
