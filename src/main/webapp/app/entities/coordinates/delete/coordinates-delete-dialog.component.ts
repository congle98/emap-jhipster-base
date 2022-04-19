import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICoordinates } from '../coordinates.model';
import { CoordinatesService } from '../service/coordinates.service';

@Component({
  templateUrl: './coordinates-delete-dialog.component.html',
})
export class CoordinatesDeleteDialogComponent {
  coordinates?: ICoordinates;

  constructor(protected coordinatesService: CoordinatesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.coordinatesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
