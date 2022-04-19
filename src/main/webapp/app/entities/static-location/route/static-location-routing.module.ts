import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StaticLocationComponent } from '../list/static-location.component';
import { StaticLocationDetailComponent } from '../detail/static-location-detail.component';
import { StaticLocationUpdateComponent } from '../update/static-location-update.component';
import { StaticLocationRoutingResolveService } from './static-location-routing-resolve.service';

const staticLocationRoute: Routes = [
  {
    path: '',
    component: StaticLocationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StaticLocationDetailComponent,
    resolve: {
      staticLocation: StaticLocationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StaticLocationUpdateComponent,
    resolve: {
      staticLocation: StaticLocationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StaticLocationUpdateComponent,
    resolve: {
      staticLocation: StaticLocationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(staticLocationRoute)],
  exports: [RouterModule],
})
export class StaticLocationRoutingModule {}
