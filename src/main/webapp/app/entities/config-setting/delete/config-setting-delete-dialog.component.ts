import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IConfigSetting } from '../config-setting.model';
import { ConfigSettingService } from '../service/config-setting.service';

@Component({
  templateUrl: './config-setting-delete-dialog.component.html',
})
export class ConfigSettingDeleteDialogComponent {
  configSetting?: IConfigSetting;

  constructor(protected configSettingService: ConfigSettingService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.configSettingService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
