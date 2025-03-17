import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGroupChat } from '../group-chat.model';
import { GroupChatService } from '../service/group-chat.service';

const groupChatResolve = (route: ActivatedRouteSnapshot): Observable<null | IGroupChat> => {
  const id = route.params.id;
  if (id) {
    return inject(GroupChatService)
      .find(id)
      .pipe(
        mergeMap((groupChat: HttpResponse<IGroupChat>) => {
          if (groupChat.body) {
            return of(groupChat.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default groupChatResolve;
