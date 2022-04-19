import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICoordinatesDetails, CoordinatesDetails } from '../coordinates-details.model';
import { CoordinatesDetailsService } from '../service/coordinates-details.service';

@Injectable({ providedIn: 'root' })
export class CoordinatesDetailsRoutingResolveService implements Resolve<ICoordinatesDetails> {
  constructor(protected service: CoordinatesDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICoordinatesDetails> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((coordinatesDetails: HttpResponse<CoordinatesDetails>) => {
          if (coordinatesDetails.body) {
            return of(coordinatesDetails.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CoordinatesDetails());
  }
}
