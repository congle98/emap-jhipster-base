import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICampaign, Campaign } from '../campaign.model';
import { CampaignService } from '../service/campaign.service';

@Component({
  selector: 'jhi-campaign-update',
  templateUrl: './campaign-update.component.html',
})
export class CampaignUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    sourceType: [null, [Validators.required, Validators.maxLength(20)]],
    mcCampaingnId: [null, [Validators.maxLength(15)]],
    tmlCampaignId: [null, [Validators.maxLength(15)]],
    icon: [null, [Validators.maxLength(225)]],
    color: [null, [Validators.maxLength(20)]],
    createDate: [null, [Validators.required]],
    createUid: [null, [Validators.required, Validators.maxLength(15)]],
    lastUpdate: [null, [Validators.required]],
    lastUpdateUid: [null, [Validators.required, Validators.maxLength(15)]],
  });

  constructor(protected campaignService: CampaignService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ campaign }) => {
      if (campaign.id === undefined) {
        const today = dayjs().startOf('day');
        campaign.createDate = today;
        campaign.lastUpdate = today;
      }

      this.updateForm(campaign);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const campaign = this.createFromForm();
    if (campaign.id !== undefined) {
      this.subscribeToSaveResponse(this.campaignService.update(campaign));
    } else {
      this.subscribeToSaveResponse(this.campaignService.create(campaign));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICampaign>>): void {
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

  protected updateForm(campaign: ICampaign): void {
    this.editForm.patchValue({
      id: campaign.id,
      sourceType: campaign.sourceType,
      mcCampaingnId: campaign.mcCampaingnId,
      tmlCampaignId: campaign.tmlCampaignId,
      icon: campaign.icon,
      color: campaign.color,
      createDate: campaign.createDate ? campaign.createDate.format(DATE_TIME_FORMAT) : null,
      createUid: campaign.createUid,
      lastUpdate: campaign.lastUpdate ? campaign.lastUpdate.format(DATE_TIME_FORMAT) : null,
      lastUpdateUid: campaign.lastUpdateUid,
    });
  }

  protected createFromForm(): ICampaign {
    return {
      ...new Campaign(),
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
