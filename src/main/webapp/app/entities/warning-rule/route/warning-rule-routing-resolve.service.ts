import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWarningRule, WarningRule } from '../warning-rule.model';
import { WarningRuleService } from '../service/warning-rule.service';

@Injectable({ providedIn: 'root' })
export class WarningRuleRoutingResolveService implements Resolve<IWarningRule> {
  constructor(protected service: WarningRuleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWarningRule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((warningRule: HttpResponse<WarningRule>) => {
          if (warningRule.body) {
            return of(warningRule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WarningRule());
  }
}
