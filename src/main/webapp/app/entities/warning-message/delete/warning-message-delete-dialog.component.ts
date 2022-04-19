import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWarningMessage } from '../warning-message.model';
import { WarningMessageService } from '../service/warning-message.service';

@Component({
  templateUrl: './warning-message-delete-dialog.component.html',
})
export class WarningMessageDeleteDialogComponent {
  warningMessage?: IWarningMessage;

  constructor(protected warningMessageService: WarningMessageService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.warningMessageService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
