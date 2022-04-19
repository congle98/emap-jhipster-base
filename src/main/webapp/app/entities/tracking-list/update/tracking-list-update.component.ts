import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITrackingList, TrackingList } from '../tracking-list.model';
import { TrackingListService } from '../service/tracking-list.service';

@Component({
  selector: 'jhi-tracking-list-update',
  templateUrl: './tracking-list-update.component.html',
})
export class TrackingListUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    mcUserId: [null, [Validators.required]],
    type: [null, [Validators.required]],
    createDate: [null, [Validators.required]],
    createUid: [null, [Validators.required, Validators.maxLength(15)]],
    lastUpdate: [null, [Validators.required]],
    lastUpdateUid: [null, [Validators.required, Validators.maxLength(15)]],
  });

  constructor(protected trackingListService: TrackingListService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trackingList }) => {
      if (trackingList.id === undefined) {
        const today = dayjs().startOf('day');
        trackingList.createDate = today;
        trackingList.lastUpdate = today;
      }

      this.updateForm(trackingList);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trackingList = this.createFromForm();
    if (trackingList.id !== undefined) {
      this.subscribeToSaveResponse(this.trackingListService.update(trackingList));
    } else {
      this.subscribeToSaveResponse(this.trackingListService.create(trackingList));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrackingList>>): void {
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

  protected updateForm(trackingList: ITrackingList): void {
    this.editForm.patchValue({
      id: trackingList.id,
      mcUserId: trackingList.mcUserId,
      type: trackingList.type,
      createDate: trackingList.createDate ? trackingList.createDate.format(DATE_TIME_FORMAT) : null,
      createUid: trackingList.createUid,
      lastUpdate: trackingList.lastUpdate ? trackingList.lastUpdate.format(DATE_TIME_FORMAT) : null,
      lastUpdateUid: trackingList.lastUpdateUid,
    });
  }

  protected createFromForm(): ITrackingList {
    return {
      ...new TrackingList(),
      id: this.editForm.get(['id'])!.value,
      mcUserId: this.editForm.get(['mcUserId'])!.value,
      type: this.editForm.get(['type'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      createUid: this.editForm.get(['createUid'])!.value,
      lastUpdate: this.editForm.get(['lastUpdate'])!.value ? dayjs(this.editForm.get(['lastUpdate'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdateUid: this.editForm.get(['lastUpdateUid'])!.value,
    };
  }
}
