import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWarningRule } from '../warning-rule.model';
import { WarningRuleService } from '../service/warning-rule.service';

@Component({
  templateUrl: './warning-rule-delete-dialog.component.html',
})
export class WarningRuleDeleteDialogComponent {
  warningRule?: IWarningRule;

  constructor(protected warningRuleService: WarningRuleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.warningRuleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
