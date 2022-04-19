import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICoordinatesDetails } from '../coordinates-details.model';
import { CoordinatesDetailsService } from '../service/coordinates-details.service';

@Component({
  templateUrl: './coordinates-details-delete-dialog.component.html',
})
export class CoordinatesDetailsDeleteDialogComponent {
  coordinatesDetails?: ICoordinatesDetails;

  constructor(protected coordinatesDetailsService: CoordinatesDetailsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.coordinatesDetailsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
