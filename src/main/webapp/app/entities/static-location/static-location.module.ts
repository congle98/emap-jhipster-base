import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { StaticLocationComponent } from './list/static-location.component';
import { StaticLocationDetailComponent } from './detail/static-location-detail.component';
import { StaticLocationUpdateComponent } from './update/static-location-update.component';
import { StaticLocationDeleteDialogComponent } from './delete/static-location-delete-dialog.component';
import { StaticLocationRoutingModule } from './route/static-location-routing.module';

@NgModule({
  imports: [SharedModule, StaticLocationRoutingModule],
  declarations: [
    StaticLocationComponent,
    StaticLocationDetailComponent,
    StaticLocationUpdateComponent,
    StaticLocationDeleteDialogComponent,
  ],
  entryComponents: [StaticLocationDeleteDialogComponent],
})
export class StaticLocationModule {}
