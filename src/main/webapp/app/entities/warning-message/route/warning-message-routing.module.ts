import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WarningMessageComponent } from '../list/warning-message.component';
import { WarningMessageDetailComponent } from '../detail/warning-message-detail.component';
import { WarningMessageUpdateComponent } from '../update/warning-message-update.component';
import { WarningMessageRoutingResolveService } from './warning-message-routing-resolve.service';

const warningMessageRoute: Routes = [
  {
    path: '',
    component: WarningMessageComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WarningMessageDetailComponent,
    resolve: {
      warningMessage: WarningMessageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WarningMessageUpdateComponent,
    resolve: {
      warningMessage: WarningMessageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WarningMessageUpdateComponent,
    resolve: {
      warningMessage: WarningMessageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(warningMessageRoute)],
  exports: [RouterModule],
})
export class WarningMessageRoutingModule {}
