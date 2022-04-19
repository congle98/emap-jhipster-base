import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CoordinatesDetailsComponent } from './list/coordinates-details.component';
import { CoordinatesDetailsDetailComponent } from './detail/coordinates-details-detail.component';
import { CoordinatesDetailsUpdateComponent } from './update/coordinates-details-update.component';
import { CoordinatesDetailsDeleteDialogComponent } from './delete/coordinates-details-delete-dialog.component';
import { CoordinatesDetailsRoutingModule } from './route/coordinates-details-routing.module';

@NgModule({
  imports: [SharedModule, CoordinatesDetailsRoutingModule],
  declarations: [
    CoordinatesDetailsComponent,
    CoordinatesDetailsDetailComponent,
    CoordinatesDetailsUpdateComponent,
    CoordinatesDetailsDeleteDialogComponent,
  ],
  entryComponents: [CoordinatesDetailsDeleteDialogComponent],
})
export class CoordinatesDetailsModule {}
