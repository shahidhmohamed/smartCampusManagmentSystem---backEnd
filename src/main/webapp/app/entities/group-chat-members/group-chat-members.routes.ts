import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import GroupChatMembersResolve from './route/group-chat-members-routing-resolve.service';

const groupChatMembersRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/group-chat-members.component').then(m => m.GroupChatMembersComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/group-chat-members-detail.component').then(m => m.GroupChatMembersDetailComponent),
    resolve: {
      groupChatMembers: GroupChatMembersResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/group-chat-members-update.component').then(m => m.GroupChatMembersUpdateComponent),
    resolve: {
      groupChatMembers: GroupChatMembersResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/group-chat-members-update.component').then(m => m.GroupChatMembersUpdateComponent),
    resolve: {
      groupChatMembers: GroupChatMembersResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default groupChatMembersRoute;
