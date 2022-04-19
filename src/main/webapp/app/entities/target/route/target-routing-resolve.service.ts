import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITarget, Target } from '../target.model';
import { TargetService } from '../service/target.service';

@Injectable({ providedIn: 'root' })
export class TargetRoutingResolveService implements Resolve<ITarget> {
  constructor(protected service: TargetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITarget> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((target: HttpResponse<Target>) => {
          if (target.body) {
            return of(target.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Target());
  }
}
