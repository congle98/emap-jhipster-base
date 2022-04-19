import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CoordinatesDetailsComponent } from '../list/coordinates-details.component';
import { CoordinatesDetailsDetailComponent } from '../detail/coordinates-details-detail.component';
import { CoordinatesDetailsUpdateComponent } from '../update/coordinates-details-update.component';
import { CoordinatesDetailsRoutingResolveService } from './coordinates-details-routing-resolve.service';

const coordinatesDetailsRoute: Routes = [
  {
    path: '',
    component: CoordinatesDetailsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CoordinatesDetailsDetailComponent,
    resolve: {
      coordinatesDetails: CoordinatesDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CoordinatesDetailsUpdateComponent,
    resolve: {
      coordinatesDetails: CoordinatesDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CoordinatesDetailsUpdateComponent,
    resolve: {
      coordinatesDetails: CoordinatesDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(coordinatesDetailsRoute)],
  exports: [RouterModule],
})
export class CoordinatesDetailsRoutingModule {}
