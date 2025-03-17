import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGroupChatMembers } from '../group-chat-members.model';
import { GroupChatMembersService } from '../service/group-chat-members.service';

const groupChatMembersResolve = (route: ActivatedRouteSnapshot): Observable<null | IGroupChatMembers> => {
  const id = route.params.id;
  if (id) {
    return inject(GroupChatMembersService)
      .find(id)
      .pipe(
        mergeMap((groupChatMembers: HttpResponse<IGroupChatMembers>) => {
          if (groupChatMembers.body) {
            return of(groupChatMembers.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default groupChatMembersResolve;
