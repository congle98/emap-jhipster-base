import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IStaticLocation, StaticLocation } from '../static-location.model';
import { StaticLocationService } from '../service/static-location.service';

@Component({
  selector: 'jhi-static-location-update',
  templateUrl: './static-location-update.component.html',
})
export class StaticLocationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(255)]],
    mcUserId: [null, [Validators.required, Validators.maxLength(15)]],
    address: [null, [Validators.required, Validators.maxLength(255)]],
    lat: [null, [Validators.required, Validators.maxLength(20)]],
    lng: [null, [Validators.required, Validators.maxLength(20)]],
    status: [null, [Validators.required]],
    createDate: [null, [Validators.required]],
    createUid: [null, [Validators.required, Validators.maxLength(15)]],
    lastUpdate: [null, [Validators.required]],
    lastUpdateUid: [null, [Validators.required, Validators.maxLength(15)]],
  });

  constructor(
    protected staticLocationService: StaticLocationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ staticLocation }) => {
      if (staticLocation.id === undefined) {
        const today = dayjs().startOf('day');
        staticLocation.createDate = today;
        staticLocation.lastUpdate = today;
      }

      this.updateForm(staticLocation);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const staticLocation = this.createFromForm();
    if (staticLocation.id !== undefined) {
      this.subscribeToSaveResponse(this.staticLocationService.update(staticLocation));
    } else {
      this.subscribeToSaveResponse(this.staticLocationService.create(staticLocation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStaticLocation>>): void {
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

  protected updateForm(staticLocation: IStaticLocation): void {
    this.editForm.patchValue({
      id: staticLocation.id,
      name: staticLocation.name,
      mcUserId: staticLocation.mcUserId,
      address: staticLocation.address,
      lat: staticLocation.lat,
      lng: staticLocation.lng,
      status: staticLocation.status,
      createDate: staticLocation.createDate ? staticLocation.createDate.format(DATE_TIME_FORMAT) : null,
      createUid: staticLocation.createUid,
      lastUpdate: staticLocation.lastUpdate ? staticLocation.lastUpdate.format(DATE_TIME_FORMAT) : null,
      lastUpdateUid: staticLocation.lastUpdateUid,
    });
  }

  protected createFromForm(): IStaticLocation {
    return {
      ...new StaticLocation(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      mcUserId: this.editForm.get(['mcUserId'])!.value,
      address: this.editForm.get(['address'])!.value,
      lat: this.editForm.get(['lat'])!.value,
      lng: this.editForm.get(['lng'])!.value,
      status: this.editForm.get(['status'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      createUid: this.editForm.get(['createUid'])!.value,
      lastUpdate: this.editForm.get(['lastUpdate'])!.value ? dayjs(this.editForm.get(['lastUpdate'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdateUid: this.editForm.get(['lastUpdateUid'])!.value,
    };
  }
}
