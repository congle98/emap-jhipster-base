import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITarget, Target } from '../target.model';
import { TargetService } from '../service/target.service';

@Component({
  selector: 'jhi-target-update',
  templateUrl: './target-update.component.html',
})
export class TargetUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    sourceType: [null, [Validators.required, Validators.maxLength(20)]],
    mcCampaingnId: [null, [Validators.maxLength(15)]],
    tmlCampaignId: [null, [Validators.maxLength(15)]],
    icon: [null, [Validators.maxLength(255)]],
    color: [null, [Validators.maxLength(20)]],
    createDate: [null, [Validators.required]],
    createUid: [null, [Validators.required, Validators.maxLength(15)]],
    lastUpdate: [null, [Validators.required]],
    lastUpdateUid: [null, [Validators.required, Validators.maxLength(15)]],
  });

  constructor(protected targetService: TargetService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ target }) => {
      if (target.id === undefined) {
        const today = dayjs().startOf('day');
        target.createDate = today;
        target.lastUpdate = today;
      }

      this.updateForm(target);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const target = this.createFromForm();
    if (target.id !== undefined) {
      this.subscribeToSaveResponse(this.targetService.update(target));
    } else {
      this.subscribeToSaveResponse(this.targetService.create(target));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITarget>>): void {
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

  protected updateForm(target: ITarget): void {
    this.editForm.patchValue({
      id: target.id,
      sourceType: target.sourceType,
      mcCampaingnId: target.mcCampaingnId,
      tmlCampaignId: target.tmlCampaignId,
      icon: target.icon,
      color: target.color,
      createDate: target.createDate ? target.createDate.format(DATE_TIME_FORMAT) : null,
      createUid: target.createUid,
      lastUpdate: target.lastUpdate ? target.lastUpdate.format(DATE_TIME_FORMAT) : null,
      lastUpdateUid: target.lastUpdateUid,
    });
  }

  protected createFromForm(): ITarget {
    return {
      ...new Target(),
      id: this.editForm.get(['id'])!.value,
      sourceType: this.editForm.get(['sourceType'])!.value,
      mcCampaingnId: this.editForm.get(['mcCampaingnId'])!.value,
      tmlCampaignId: this.editForm.get(['tmlCampaignId'])!.value,
      icon: this.editForm.get(['icon'])!.value,
      color: this.editForm.get(['color'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      createUid: this.editForm.get(['createUid'])!.value,
      lastUpdate: this.editForm.get(['lastUpdate'])!.value ? dayjs(this.editForm.get(['lastUpdate'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdateUid: this.editForm.get(['lastUpdateUid'])!.value,
    };
  }
}
