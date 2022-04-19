import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TrackingListDetailsComponent } from './list/tracking-list-details.component';
import { TrackingListDetailsDetailComponent } from './detail/tracking-list-details-detail.component';
import { TrackingListDetailsUpdateComponent } from './update/tracking-list-details-update.component';
import { TrackingListDetailsDeleteDialogComponent } from './delete/tracking-list-details-delete-dialog.component';
import { TrackingListDetailsRoutingModule } from './route/tracking-list-details-routing.module';

@NgModule({
  imports: [SharedModule, TrackingListDetailsRoutingModule],
  declarations: [
    TrackingListDetailsComponent,
    TrackingListDetailsDetailComponent,
    TrackingListDetailsUpdateComponent,
    TrackingListDetailsDeleteDialogComponent,
  ],
  entryComponents: [TrackingListDetailsDeleteDialogComponent],
})
export class TrackingListDetailsModule {}
