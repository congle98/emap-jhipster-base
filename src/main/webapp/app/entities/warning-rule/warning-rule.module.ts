import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WarningRuleComponent } from './list/warning-rule.component';
import { WarningRuleDetailComponent } from './detail/warning-rule-detail.component';
import { WarningRuleUpdateComponent } from './update/warning-rule-update.component';
import { WarningRuleDeleteDialogComponent } from './delete/warning-rule-delete-dialog.component';
import { WarningRuleRoutingModule } from './route/warning-rule-routing.module';

@NgModule({
  imports: [SharedModule, WarningRuleRoutingModule],
  declarations: [WarningRuleComponent, WarningRuleDetailComponent, WarningRuleUpdateComponent, WarningRuleDeleteDialogComponent],
  entryComponents: [WarningRuleDeleteDialogComponent],
})
export class WarningRuleModule {}
