import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import GroupChatResolve from './route/group-chat-routing-resolve.service';

const groupChatRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/group-chat.component').then(m => m.GroupChatComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/group-chat-detail.component').then(m => m.GroupChatDetailComponent),
    resolve: {
      groupChat: GroupChatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/group-chat-update.component').then(m => m.GroupChatUpdateComponent),
    resolve: {
      groupChat: GroupChatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/group-chat-update.component').then(m => m.GroupChatUpdateComponent),
    resolve: {
      groupChat: GroupChatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default groupChatRoute;
