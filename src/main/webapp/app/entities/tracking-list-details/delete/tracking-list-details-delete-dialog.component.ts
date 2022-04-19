import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITrackingListDetails } from '../tracking-list-details.model';
import { TrackingListDetailsService } from '../service/tracking-list-details.service';

@Component({
  templateUrl: './tracking-list-details-delete-dialog.component.html',
})
export class TrackingListDetailsDeleteDialogComponent {
  trackingListDetails?: ITrackingListDetails;

  constructor(protected trackingListDetailsService: TrackingListDetailsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trackingListDetailsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
