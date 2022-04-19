import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ConfigSettingComponent } from './list/config-setting.component';
import { ConfigSettingDetailComponent } from './detail/config-setting-detail.component';
import { ConfigSettingUpdateComponent } from './update/config-setting-update.component';
import { ConfigSettingDeleteDialogComponent } from './delete/config-setting-delete-dialog.component';
import { ConfigSettingRoutingModule } from './route/config-setting-routing.module';

@NgModule({
  imports: [SharedModule, ConfigSettingRoutingModule],
  declarations: [ConfigSettingComponent, ConfigSettingDetailComponent, ConfigSettingUpdateComponent, ConfigSettingDeleteDialogComponent],
  entryComponents: [ConfigSettingDeleteDialogComponent],
})
export class ConfigSettingModule {}
