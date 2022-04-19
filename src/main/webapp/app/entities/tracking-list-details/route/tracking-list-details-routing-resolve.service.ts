import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrackingListDetails, TrackingListDetails } from '../tracking-list-details.model';
import { TrackingListDetailsService } from '../service/tracking-list-details.service';

@Injectable({ providedIn: 'root' })
export class TrackingListDetailsRoutingResolveService implements Resolve<ITrackingListDetails> {
  constructor(protected service: TrackingListDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITrackingListDetails> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((trackingListDetails: HttpResponse<TrackingListDetails>) => {
          if (trackingListDetails.body) {
            return of(trackingListDetails.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TrackingListDetails());
  }
}
