import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICoordinates, Coordinates } from '../coordinates.model';
import { CoordinatesService } from '../service/coordinates.service';

@Injectable({ providedIn: 'root' })
export class CoordinatesRoutingResolveService implements Resolve<ICoordinates> {
  constructor(protected service: CoordinatesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICoordinates> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((coordinates: HttpResponse<Coordinates>) => {
          if (coordinates.body) {
            return of(coordinates.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Coordinates());
  }
}
