import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IWarningMessage, WarningMessage } from '../warning-message.model';
import { WarningMessageService } from '../service/warning-message.service';
import { IWarningRule } from 'app/entities/warning-rule/warning-rule.model';
import { WarningRuleService } from 'app/entities/warning-rule/service/warning-rule.service';

@Component({
  selector: 'jhi-warning-message-update',
  templateUrl: './warning-message-update.component.html',
})
export class WarningMessageUpdateComponent implements OnInit {
  isSaving = false;

  warningRulesCollection: IWarningRule[] = [];

  editForm = this.fb.group({
    id: [],
    mcUserId: [null, [Validators.required, Validators.maxLength(15)]],
    warningDistance: [null, [Validators.required]],
    showWarningCircle: [null, [Validators.required]],
    showWarningMessage: [null, [Validators.required]],
    warningMessage: [null, [Validators.maxLength(255)]],
    sendWarningMessageToMc: [null, [Validators.required]],
    createDate: [null, [Validators.required]],
    createUid: [null, [Validators.required, Validators.maxLength(15)]],
    lastUpdate: [null, [Validators.required]],
    lastUpdateUid: [null, [Validators.required, Validators.maxLength(15)]],
    warningRule: [null, Validators.required],
  });

  constructor(
    protected warningMessageService: WarningMessageService,
    protected warningRuleService: WarningRuleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ warningMessage }) => {
      if (warningMessage.id === undefined) {
        const today = dayjs().startOf('day');
        warningMessage.createDate = today;
        warningMessage.lastUpdate = today;
      }

      this.updateForm(warningMessage);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const warningMessage = this.createFromForm();
    if (warningMessage.id !== undefined) {
      this.subscribeToSaveResponse(this.warningMessageService.update(warningMessage));
    } else {
      this.subscribeToSaveResponse(this.warningMessageService.create(warningMessage));
    }
  }

  trackWarningRuleById(_index: number, item: IWarningRule): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWarningMessage>>): void {
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

  protected updateForm(warningMessage: IWarningMessage): void {
    this.editForm.patchValue({
      id: warningMessage.id,
      mcUserId: warningMessage.mcUserId,
      warningDistance: warningMessage.warningDistance,
      showWarningCircle: warningMessage.showWarningCircle,
      showWarningMessage: warningMessage.showWarningMessage,
      warningMessage: warningMessage.warningMessage,
      sendWarningMessageToMc: warningMessage.sendWarningMessageToMc,
      createDate: warningMessage.createDate ? warningMessage.createDate.format(DATE_TIME_FORMAT) : null,
      createUid: warningMessage.createUid,
      lastUpdate: warningMessage.lastUpdate ? warningMessage.lastUpdate.format(DATE_TIME_FORMAT) : null,
      lastUpdateUid: warningMessage.lastUpdateUid,
      warningRule: warningMessage.warningRule,
    });

    this.warningRulesCollection = this.warningRuleService.addWarningRuleToCollectionIfMissing(
      this.warningRulesCollection,
      warningMessage.warningRule
    );
  }

  protected loadRelationshipsOptions(): void {
    this.warningRuleService
      .query({ filter: 'warningmessage-is-null' })
      .pipe(map((res: HttpResponse<IWarningRule[]>) => res.body ?? []))
      .pipe(
        map((warningRules: IWarningRule[]) =>
          this.warningRuleService.addWarningRuleToCollectionIfMissing(warningRules, this.editForm.get('warningRule')!.value)
        )
      )
      .subscribe((warningRules: IWarningRule[]) => (this.warningRulesCollection = warningRules));
  }

  protected createFromForm(): IWarningMessage {
    return {
      ...new WarningMessage(),
      id: this.editForm.get(['id'])!.value,
      mcUserId: this.editForm.get(['mcUserId'])!.value,
      warningDistance: this.editForm.get(['warningDistance'])!.value,
      showWarningCircle: this.editForm.get(['showWarningCircle'])!.value,
      showWarningMessage: this.editForm.get(['showWarningMessage'])!.value,
      warningMessage: this.editForm.get(['warningMessage'])!.value,
      sendWarningMessageToMc: this.editForm.get(['sendWarningMessageToMc'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      createUid: this.editForm.get(['createUid'])!.value,
      lastUpdate: this.editForm.get(['lastUpdate'])!.value ? dayjs(this.editForm.get(['lastUpdate'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdateUid: this.editForm.get(['lastUpdateUid'])!.value,
      warningRule: this.editForm.get(['warningRule'])!.value,
    };
  }
}
