import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'campaign',
        data: { pageTitle: 'emapApp.campaign.home.title' },
        loadChildren: () => import('./campaign/campaign.module').then(m => m.CampaignModule),
      },
      {
        path: 'target',
        data: { pageTitle: 'emapApp.target.home.title' },
        loadChildren: () => import('./target/target.module').then(m => m.TargetModule),
      },
      {
        path: 'coordinates',
        data: { pageTitle: 'emapApp.coordinates.home.title' },
        loadChildren: () => import('./coordinates/coordinates.module').then(m => m.CoordinatesModule),
      },
      {
        path: 'coordinates-details',
        data: { pageTitle: 'emapApp.coordinatesDetails.home.title' },
        loadChildren: () => import('./coordinates-details/coordinates-details.module').then(m => m.CoordinatesDetailsModule),
      },
      {
        path: 'tracking-list',
        data: { pageTitle: 'emapApp.trackingList.home.title' },
        loadChildren: () => import('./tracking-list/tracking-list.module').then(m => m.TrackingListModule),
      },
      {
        path: 'tracking-list-details',
        data: { pageTitle: 'emapApp.trackingListDetails.home.title' },
        loadChildren: () => import('./tracking-list-details/tracking-list-details.module').then(m => m.TrackingListDetailsModule),
      },
      {
        path: 'static-location',
        data: { pageTitle: 'emapApp.staticLocation.home.title' },
        loadChildren: () => import('./static-location/static-location.module').then(m => m.StaticLocationModule),
      },
      {
        path: 'warning-rule',
        data: { pageTitle: 'emapApp.warningRule.home.title' },
        loadChildren: () => import('./warning-rule/warning-rule.module').then(m => m.WarningRuleModule),
      },
      {
        path: 'warning-message',
        data: { pageTitle: 'emapApp.warningMessage.home.title' },
        loadChildren: () => import('./warning-message/warning-message.module').then(m => m.WarningMessageModule),
      },
      {
        path: 'config-setting',
        data: { pageTitle: 'emapApp.configSetting.home.title' },
        loadChildren: () => import('./config-setting/config-setting.module').then(m => m.ConfigSettingModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
