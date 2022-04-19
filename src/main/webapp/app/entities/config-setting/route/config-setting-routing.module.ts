import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConfigSettingComponent } from '../list/config-setting.component';
import { ConfigSettingDetailComponent } from '../detail/config-setting-detail.component';
import { ConfigSettingUpdateComponent } from '../update/config-setting-update.component';
import { ConfigSettingRoutingResolveService } from './config-setting-routing-resolve.service';

const configSettingRoute: Routes = [
  {
    path: '',
    component: ConfigSettingComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConfigSettingDetailComponent,
    resolve: {
      configSetting: ConfigSettingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConfigSettingUpdateComponent,
    resolve: {
      configSetting: ConfigSettingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConfigSettingUpdateComponent,
    resolve: {
      configSetting: ConfigSettingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(configSettingRoute)],
  exports: [RouterModule],
})
export class ConfigSettingRoutingModule {}
