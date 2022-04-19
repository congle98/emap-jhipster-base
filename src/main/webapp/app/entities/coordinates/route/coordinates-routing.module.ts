import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CoordinatesComponent } from '../list/coordinates.component';
import { CoordinatesDetailComponent } from '../detail/coordinates-detail.component';
import { CoordinatesUpdateComponent } from '../update/coordinates-update.component';
import { CoordinatesRoutingResolveService } from './coordinates-routing-resolve.service';

const coordinatesRoute: Routes = [
  {
    path: '',
    component: CoordinatesComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CoordinatesDetailComponent,
    resolve: {
      coordinates: CoordinatesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CoordinatesUpdateComponent,
    resolve: {
      coordinates: CoordinatesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CoordinatesUpdateComponent,
    resolve: {
      coordinates: CoordinatesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(coordinatesRoute)],
  exports: [RouterModule],
})
export class CoordinatesRoutingModule {}
