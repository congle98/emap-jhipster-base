import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITrackingListDetails, TrackingListDetails } from '../tracking-list-details.model';
import { TrackingListDetailsService } from '../service/tracking-list-details.service';
import { ITrackingList } from 'app/entities/tracking-list/tracking-list.model';
import { TrackingListService } from 'app/entities/tracking-list/service/tracking-list.service';
import { ITarget } from 'app/entities/target/target.model';
import { TargetService } from 'app/entities/target/service/target.service';

@Component({
  selector: 'jhi-tracking-list-details-update',
  templateUrl: './tracking-list-details-update.component.html',
})
export class TrackingListDetailsUpdateComponent implements OnInit {
  isSaving = false;

  trackingListsCollection: ITrackingList[] = [];
  mcTargetsCollection: ITarget[] = [];

  editForm = this.fb.group({
    id: [],
    createDate: [null, [Validators.required]],
    createUid: [null, [Validators.required, Validators.maxLength(15)]],
    lastUpdate: [null, [Validators.required]],
    lastUpdateUid: [null, [Validators.required, Validators.maxLength(15)]],
    trackingList: [null, Validators.required],
    mcTarget: [null, Validators.required],
  });

  constructor(
    protected trackingListDetailsService: TrackingListDetailsService,
    protected trackingListService: TrackingListService,
    protected targetService: TargetService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trackingListDetails }) => {
      if (trackingListDetails.id === undefined) {
        const today = dayjs().startOf('day');
        trackingListDetails.createDate = today;
        trackingListDetails.lastUpdate = today;
      }

      this.updateForm(trackingListDetails);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trackingListDetails = this.createFromForm();
    if (trackingListDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.trackingListDetailsService.update(trackingListDetails));
    } else {
      this.subscribeToSaveResponse(this.trackingListDetailsService.create(trackingListDetails));
    }
  }

  trackTrackingListById(_index: number, item: ITrackingList): number {
    return item.id!;
  }

  trackTargetById(_index: number, item: ITarget): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrackingListDetails>>): void {
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

  protected updateForm(trackingListDetails: ITrackingListDetails): void {
    this.editForm.patchValue({
      id: trackingListDetails.id,
      createDate: trackingListDetails.createDate ? trackingListDetails.createDate.format(DATE_TIME_FORMAT) : null,
      createUid: trackingListDetails.createUid,
      lastUpdate: trackingListDetails.lastUpdate ? trackingListDetails.lastUpdate.format(DATE_TIME_FORMAT) : null,
      lastUpdateUid: trackingListDetails.lastUpdateUid,
      trackingList: trackingListDetails.trackingList,
      mcTarget: trackingListDetails.mcTarget,
    });

    this.trackingListsCollection = this.trackingListService.addTrackingListToCollectionIfMissing(
      this.trackingListsCollection,
      trackingListDetails.trackingList
    );
    this.mcTargetsCollection = this.targetService.addTargetToCollectionIfMissing(this.mcTargetsCollection, trackingListDetails.mcTarget);
  }

  protected loadRelationshipsOptions(): void {
    this.trackingListService
      .query({ filter: 'trackinglistdetails-is-null' })
      .pipe(map((res: HttpResponse<ITrackingList[]>) => res.body ?? []))
      .pipe(
        map((trackingLists: ITrackingList[]) =>
          this.trackingListService.addTrackingListToCollectionIfMissing(trackingLists, this.editForm.get('trackingList')!.value)
        )
      )
      .subscribe((trackingLists: ITrackingList[]) => (this.trackingListsCollection = trackingLists));

    this.targetService
      .query({ filter: 'trackinglistdetails-is-null' })
      .pipe(map((res: HttpResponse<ITarget[]>) => res.body ?? []))
      .pipe(map((targets: ITarget[]) => this.targetService.addTargetToCollectionIfMissing(targets, this.editForm.get('mcTarget')!.value)))
      .subscribe((targets: ITarget[]) => (this.mcTargetsCollection = targets));
  }

  protected createFromForm(): ITrackingListDetails {
    return {
      ...new TrackingListDetails(),
      id: this.editForm.get(['id'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      createUid: this.editForm.get(['createUid'])!.value,
      lastUpdate: this.editForm.get(['lastUpdate'])!.value ? dayjs(this.editForm.get(['lastUpdate'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdateUid: this.editForm.get(['lastUpdateUid'])!.value,
      trackingList: this.editForm.get(['trackingList'])!.value,
      mcTarget: this.editForm.get(['mcTarget'])!.value,
    };
  }
}
