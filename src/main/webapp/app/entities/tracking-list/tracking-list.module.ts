import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TrackingListComponent } from './list/tracking-list.component';
import { TrackingListDetailComponent } from './detail/tracking-list-detail.component';
import { TrackingListUpdateComponent } from './update/tracking-list-update.component';
import { TrackingListDeleteDialogComponent } from './delete/tracking-list-delete-dialog.component';
import { TrackingListRoutingModule } from './route/tracking-list-routing.module';

@NgModule({
  imports: [SharedModule, TrackingListRoutingModule],
  declarations: [TrackingListComponent, TrackingListDetailComponent, TrackingListUpdateComponent, TrackingListDeleteDialogComponent],
  entryComponents: [TrackingListDeleteDialogComponent],
})
export class TrackingListModule {}
