import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStaticLocation } from '../static-location.model';
import { StaticLocationService } from '../service/static-location.service';

@Component({
  templateUrl: './static-location-delete-dialog.component.html',
})
export class StaticLocationDeleteDialogComponent {
  staticLocation?: IStaticLocation;

  constructor(protected staticLocationService: StaticLocationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.staticLocationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
