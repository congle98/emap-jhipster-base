import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICoordinatesDetails, CoordinatesDetails } from '../coordinates-details.model';
import { CoordinatesDetailsService } from '../service/coordinates-details.service';
import { ICoordinates } from 'app/entities/coordinates/coordinates.model';
import { CoordinatesService } from 'app/entities/coordinates/service/coordinates.service';
import { ITarget } from 'app/entities/target/target.model';
import { TargetService } from 'app/entities/target/service/target.service';

@Component({
  selector: 'jhi-coordinates-details-update',
  templateUrl: './coordinates-details-update.component.html',
})
export class CoordinatesDetailsUpdateComponent implements OnInit {
  isSaving = false;

  coordinatesCollection: ICoordinates[] = [];
  objectsCollection: ITarget[] = [];

  editForm = this.fb.group({
    id: [],
    signalConnectionStrength: [],
    createDate: [null, [Validators.required]],
    createUid: [null, [Validators.required, Validators.maxLength(15)]],
    lastUpdate: [null, [Validators.required]],
    lastUpdateUid: [null, [Validators.required, Validators.maxLength(15)]],
    coordinate: [null, Validators.required],
    object: [null, Validators.required],
  });

  constructor(
    protected coordinatesDetailsService: CoordinatesDetailsService,
    protected coordinatesService: CoordinatesService,
    protected targetService: TargetService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ coordinatesDetails }) => {
      if (coordinatesDetails.id === undefined) {
        const today = dayjs().startOf('day');
        coordinatesDetails.createDate = today;
        coordinatesDetails.lastUpdate = today;
      }

      this.updateForm(coordinatesDetails);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const coordinatesDetails = this.createFromForm();
    if (coordinatesDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.coordinatesDetailsService.update(coordinatesDetails));
    } else {
      this.subscribeToSaveResponse(this.coordinatesDetailsService.create(coordinatesDetails));
    }
  }

  trackCoordinatesById(_index: number, item: ICoordinates): number {
    return item.id!;
  }

  trackTargetById(_index: number, item: ITarget): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICoordinatesDetails>>): void {
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

  protected updateForm(coordinatesDetails: ICoordinatesDetails): void {
    this.editForm.patchValue({
      id: coordinatesDetails.id,
      signalConnectionStrength: coordinatesDetails.signalConnectionStrength,
      createDate: coordinatesDetails.createDate ? coordinatesDetails.createDate.format(DATE_TIME_FORMAT) : null,
      createUid: coordinatesDetails.createUid,
      lastUpdate: coordinatesDetails.lastUpdate ? coordinatesDetails.lastUpdate.format(DATE_TIME_FORMAT) : null,
      lastUpdateUid: coordinatesDetails.lastUpdateUid,
      coordinate: coordinatesDetails.coordinate,
      object: coordinatesDetails.object,
    });

    this.coordinatesCollection = this.coordinatesService.addCoordinatesToCollectionIfMissing(
      this.coordinatesCollection,
      coordinatesDetails.coordinate
    );
    this.objectsCollection = this.targetService.addTargetToCollectionIfMissing(this.objectsCollection, coordinatesDetails.object);
  }

  protected loadRelationshipsOptions(): void {
    this.coordinatesService
      .query({ filter: 'coordinatesdetails-is-null' })
      .pipe(map((res: HttpResponse<ICoordinates[]>) => res.body ?? []))
      .pipe(
        map((coordinates: ICoordinates[]) =>
          this.coordinatesService.addCoordinatesToCollectionIfMissing(coordinates, this.editForm.get('coordinate')!.value)
        )
      )
      .subscribe((coordinates: ICoordinates[]) => (this.coordinatesCollection = coordinates));

    this.targetService
      .query({ filter: 'coordinatesdetails-is-null' })
      .pipe(map((res: HttpResponse<ITarget[]>) => res.body ?? []))
      .pipe(map((targets: ITarget[]) => this.targetService.addTargetToCollectionIfMissing(targets, this.editForm.get('object')!.value)))
      .subscribe((targets: ITarget[]) => (this.objectsCollection = targets));
  }

  protected createFromForm(): ICoordinatesDetails {
    return {
      ...new CoordinatesDetails(),
      id: this.editForm.get(['id'])!.value,
      signalConnectionStrength: this.editForm.get(['signalConnectionStrength'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      createUid: this.editForm.get(['createUid'])!.value,
      lastUpdate: this.editForm.get(['lastUpdate'])!.value ? dayjs(this.editForm.get(['lastUpdate'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdateUid: this.editForm.get(['lastUpdateUid'])!.value,
      coordinate: this.editForm.get(['coordinate'])!.value,
      object: this.editForm.get(['object'])!.value,
    };
  }
}
