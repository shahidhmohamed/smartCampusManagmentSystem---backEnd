import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChatUser } from '../chat-user.model';
import { ChatUserService } from '../service/chat-user.service';

const chatUserResolve = (route: ActivatedRouteSnapshot): Observable<null | IChatUser> => {
  const id = route.params.id;
  if (id) {
    return inject(ChatUserService)
      .find(id)
      .pipe(
        mergeMap((chatUser: HttpResponse<IChatUser>) => {
          if (chatUser.body) {
            return of(chatUser.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default chatUserResolve;
