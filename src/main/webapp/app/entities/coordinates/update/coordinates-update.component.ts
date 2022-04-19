import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICoordinates, Coordinates } from '../coordinates.model';
import { CoordinatesService } from '../service/coordinates.service';

@Component({
  selector: 'jhi-coordinates-update',
  templateUrl: './coordinates-update.component.html',
})
export class CoordinatesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    sourceType: [null, [Validators.required, Validators.maxLength(20)]],
    mcCampaingnId: [null, [Validators.maxLength(15)]],
    tmlCampaignId: [null, [Validators.maxLength(15)]],
    lat: [null, [Validators.maxLength(15)]],
    lng: [null, [Validators.maxLength(15)]],
    radius: [],
    openAngle: [],
    directionalAngle: [],
    createDate: [null, [Validators.required]],
    createUid: [null, [Validators.required, Validators.maxLength(15)]],
    lastUpdate: [null, [Validators.required]],
    lastUpdateUid: [null, [Validators.required, Validators.maxLength(15)]],
  });

  constructor(protected coordinatesService: CoordinatesService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ coordinates }) => {
      if (coordinates.id === undefined) {
        const today = dayjs().startOf('day');
        coordinates.createDate = today;
        coordinates.lastUpdate = today;
      }

      this.updateForm(coordinates);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const coordinates = this.createFromForm();
    if (coordinates.id !== undefined) {
      this.subscribeToSaveResponse(this.coordinatesService.update(coordinates));
    } else {
      this.subscribeToSaveResponse(this.coordinatesService.create(coordinates));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICoordinates>>): void {
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

  protected updateForm(coordinates: ICoordinates): void {
    this.editForm.patchValue({
      id: coordinates.id,
      sourceType: coordinates.sourceType,
      mcCampaingnId: coordinates.mcCampaingnId,
      tmlCampaignId: coordinates.tmlCampaignId,
      lat: coordinates.lat,
      lng: coordinates.lng,
      radius: coordinates.radius,
      openAngle: coordinates.openAngle,
      directionalAngle: coordinates.directionalAngle,
      createDate: coordinates.createDate ? coordinates.createDate.format(DATE_TIME_FORMAT) : null,
      createUid: coordinates.createUid,
      lastUpdate: coordinates.lastUpdate ? coordinates.lastUpdate.format(DATE_TIME_FORMAT) : null,
      lastUpdateUid: coordinates.lastUpdateUid,
    });
  }

  protected createFromForm(): ICoordinates {
    return {
      ...new Coordinates(),
      id: this.editForm.get(['id'])!.value,
      sourceType: this.editForm.get(['sourceType'])!.value,
      mcCampaingnId: this.editForm.get(['mcCampaingnId'])!.value,
      tmlCampaignId: this.editForm.get(['tmlCampaignId'])!.value,
      lat: this.editForm.get(['lat'])!.value,
      lng: this.editForm.get(['lng'])!.value,
      radius: this.editForm.get(['radius'])!.value,
      openAngle: this.editForm.get(['openAngle'])!.value,
      directionalAngle: this.editForm.get(['directionalAngle'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      createUid: this.editForm.get(['createUid'])!.value,
      lastUpdate: this.editForm.get(['lastUpdate'])!.value ? dayjs(this.editForm.get(['lastUpdate'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdateUid: this.editForm.get(['lastUpdateUid'])!.value,
    };
  }
}
