import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IWarningRule, WarningRule } from '../warning-rule.model';
import { WarningRuleService } from '../service/warning-rule.service';

@Component({
  selector: 'jhi-warning-rule-update',
  templateUrl: './warning-rule-update.component.html',
})
export class WarningRuleUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(255)]],
    mcUserId: [null, [Validators.required, Validators.maxLength(15)]],
    delayCheck: [null, [Validators.required]],
    delayCheckUnit: [null, [Validators.required, Validators.maxLength(15)]],
    conditionType: [null, [Validators.required, Validators.maxLength(20)]],
    includeMcCampaignId: [null, [Validators.maxLength(15)]],
    includeMcTargetId: [null, [Validators.maxLength(15)]],
    warningDistance: [null, [Validators.required]],
    showWarningCircle: [null, [Validators.required]],
    showWarningMessage: [null, [Validators.required]],
    warningMessage: [null, [Validators.maxLength(255)]],
    sendWarningMessageToMc: [null, [Validators.required]],
    status: [null, [Validators.required]],
    createDate: [null, [Validators.required]],
    createUid: [null, [Validators.required, Validators.maxLength(15)]],
    lastUpdate: [null, [Validators.required]],
    lastUpdateUid: [null, [Validators.required, Validators.maxLength(15)]],
  });

  constructor(protected warningRuleService: WarningRuleService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ warningRule }) => {
      if (warningRule.id === undefined) {
        const today = dayjs().startOf('day');
        warningRule.createDate = today;
        warningRule.lastUpdate = today;
      }

      this.updateForm(warningRule);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const warningRule = this.createFromForm();
    if (warningRule.id !== undefined) {
      this.subscribeToSaveResponse(this.warningRuleService.update(warningRule));
    } else {
      this.subscribeToSaveResponse(this.warningRuleService.create(warningRule));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWarningRule>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(warningRule: IWarningRule): void {
    this.editForm.patchValue({
      id: warningRule.id,
      name: warningRule.name,
      mcUserId: warningRule.mcUserId,
      delayCheck: warningRule.delayCheck,
      delayCheckUnit: warningRule.delayCheckUnit,
      conditionType: warningRule.conditionType,
      includeMcCampaignId: warningRule.includeMcCampaignId,
      includeMcTargetId: warningRule.includeMcTargetId,
      warningDistance: warningRule.warningDistance,
      showWarningCircle: warningRule.showWarningCircle,
      showWarningMessage: warningRule.showWarningMessage,
      warningMessage: warningRule.warningMessage,
      sendWarningMessageToMc: warningRule.sendWarningMessageToMc,
      status: warningRule.status,
      createDate: warningRule.createDate ? warningRule.createDate.format(DATE_TIME_FORMAT) : null,
      createUid: warningRule.createUid,
      lastUpdate: warningRule.lastUpdate ? warningRule.lastUpdate.format(DATE_TIME_FORMAT) : null,
      lastUpdateUid: warningRule.lastUpdateUid,
    });
  }

  protected createFromForm(): IWarningRule {
    return {
      ...new WarningRule(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      mcUserId: this.editForm.get(['mcUserId'])!.value,
      delayCheck: this.editForm.get(['delayCheck'])!.value,
      delayCheckUnit: this.editForm.get(['delayCheckUnit'])!.value,
      conditionType: this.editForm.get(['conditionType'])!.value,
      includeMcCampaignId: this.editForm.get(['includeMcCampaignId'])!.value,
      includeMcTargetId: this.editForm.get(['includeMcTargetId'])!.value,
      warningDistance: this.editForm.get(['warningDistance'])!.value,
      showWarningCircle: this.editForm.get(['showWarningCircle'])!.value,
      showWarningMessage: this.editForm.get(['showWarningMessage'])!.value,
      warningMessage: this.editForm.get(['warningMessage'])!.value,
      sendWarningMessageToMc: this.editForm.get(['sendWarningMessageToMc'])!.value,
      status: this.editForm.get(['status'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      createUid: this.editForm.get(['createUid'])!.value,
      lastUpdate: this.editForm.get(['lastUpdate'])!.value ? dayjs(this.editForm.get(['lastUpdate'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdateUid: this.editForm.get(['lastUpdateUid'])!.value,
    };
  }
}
