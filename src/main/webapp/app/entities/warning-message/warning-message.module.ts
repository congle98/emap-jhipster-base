import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WarningMessageComponent } from './list/warning-message.component';
import { WarningMessageDetailComponent } from './detail/warning-message-detail.component';
import { WarningMessageUpdateComponent } from './update/warning-message-update.component';
import { WarningMessageDeleteDialogComponent } from './delete/warning-message-delete-dialog.component';
import { WarningMessageRoutingModule } from './route/warning-message-routing.module';

@NgModule({
  imports: [SharedModule, WarningMessageRoutingModule],
  declarations: [
    WarningMessageComponent,
    WarningMessageDetailComponent,
    WarningMessageUpdateComponent,
    WarningMessageDeleteDialogComponent,
  ],
  entryComponents: [WarningMessageDeleteDialogComponent],
})
export class WarningMessageModule {}
