import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrackingList, TrackingList } from '../tracking-list.model';
import { TrackingListService } from '../service/tracking-list.service';

@Injectable({ providedIn: 'root' })
export class TrackingListRoutingResolveService implements Resolve<ITrackingList> {
  constructor(protected service: TrackingListService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITrackingList> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((trackingList: HttpResponse<TrackingList>) => {
          if (trackingList.body) {
            return of(trackingList.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TrackingList());
  }
}
