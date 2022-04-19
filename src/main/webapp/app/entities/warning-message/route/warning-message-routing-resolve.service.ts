import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWarningMessage, WarningMessage } from '../warning-message.model';
import { WarningMessageService } from '../service/warning-message.service';

@Injectable({ providedIn: 'root' })
export class WarningMessageRoutingResolveService implements Resolve<IWarningMessage> {
  constructor(protected service: WarningMessageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWarningMessage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((warningMessage: HttpResponse<WarningMessage>) => {
          if (warningMessage.body) {
            return of(warningMessage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WarningMessage());
  }
}
