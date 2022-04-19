import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TrackingListComponent } from '../list/tracking-list.component';
import { TrackingListDetailComponent } from '../detail/tracking-list-detail.component';
import { TrackingListUpdateComponent } from '../update/tracking-list-update.component';
import { TrackingListRoutingResolveService } from './tracking-list-routing-resolve.service';

const trackingListRoute: Routes = [
  {
    path: '',
    component: TrackingListComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TrackingListDetailComponent,
    resolve: {
      trackingList: TrackingListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TrackingListUpdateComponent,
    resolve: {
      trackingList: TrackingListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TrackingListUpdateComponent,
    resolve: {
      trackingList: TrackingListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(trackingListRoute)],
  exports: [RouterModule],
})
export class TrackingListRoutingModule {}
