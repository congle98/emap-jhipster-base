import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStaticLocation, StaticLocation } from '../static-location.model';
import { StaticLocationService } from '../service/static-location.service';

@Injectable({ providedIn: 'root' })
export class StaticLocationRoutingResolveService implements Resolve<IStaticLocation> {
  constructor(protected service: StaticLocationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStaticLocation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((staticLocation: HttpResponse<StaticLocation>) => {
          if (staticLocation.body) {
            return of(staticLocation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StaticLocation());
  }
}
