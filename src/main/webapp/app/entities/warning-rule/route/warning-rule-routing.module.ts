import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WarningRuleComponent } from '../list/warning-rule.component';
import { WarningRuleDetailComponent } from '../detail/warning-rule-detail.component';
import { WarningRuleUpdateComponent } from '../update/warning-rule-update.component';
import { WarningRuleRoutingResolveService } from './warning-rule-routing-resolve.service';

const warningRuleRoute: Routes = [
  {
    path: '',
    component: WarningRuleComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WarningRuleDetailComponent,
    resolve: {
      warningRule: WarningRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WarningRuleUpdateComponent,
    resolve: {
      warningRule: WarningRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WarningRuleUpdateComponent,
    resolve: {
      warningRule: WarningRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(warningRuleRoute)],
  exports: [RouterModule],
})
export class WarningRuleRoutingModule {}
