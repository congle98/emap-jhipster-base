import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITrackingList } from '../tracking-list.model';
import { TrackingListService } from '../service/tracking-list.service';

@Component({
  templateUrl: './tracking-list-delete-dialog.component.html',
})
export class TrackingListDeleteDialogComponent {
  trackingList?: ITrackingList;

  constructor(protected trackingListService: TrackingListService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trackingListService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
