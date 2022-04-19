import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CoordinatesComponent } from './list/coordinates.component';
import { CoordinatesDetailComponent } from './detail/coordinates-detail.component';
import { CoordinatesUpdateComponent } from './update/coordinates-update.component';
import { CoordinatesDeleteDialogComponent } from './delete/coordinates-delete-dialog.component';
import { CoordinatesRoutingModule } from './route/coordinates-routing.module';

@NgModule({
  imports: [SharedModule, CoordinatesRoutingModule],
  declarations: [CoordinatesComponent, CoordinatesDetailComponent, CoordinatesUpdateComponent, CoordinatesDeleteDialogComponent],
  entryComponents: [CoordinatesDeleteDialogComponent],
})
export class CoordinatesModule {}
