import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TrackingListDetailsComponent } from '../list/tracking-list-details.component';
import { TrackingListDetailsDetailComponent } from '../detail/tracking-list-details-detail.component';
import { TrackingListDetailsUpdateComponent } from '../update/tracking-list-details-update.component';
import { TrackingListDetailsRoutingResolveService } from './tracking-list-details-routing-resolve.service';

const trackingListDetailsRoute: Routes = [
  {
    path: '',
    component: TrackingListDetailsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TrackingListDetailsDetailComponent,
    resolve: {
      trackingListDetails: TrackingListDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TrackingListDetailsUpdateComponent,
    resolve: {
      trackingListDetails: TrackingListDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TrackingListDetailsUpdateComponent,
    resolve: {
      trackingListDetails: TrackingListDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(trackingListDetailsRoute)],
  exports: [RouterModule],
})
export class TrackingListDetailsRoutingModule {}
