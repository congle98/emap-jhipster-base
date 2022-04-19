import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IConfigSetting, ConfigSetting } from '../config-setting.model';
import { ConfigSettingService } from '../service/config-setting.service';

@Component({
  selector: 'jhi-config-setting-update',
  templateUrl: './config-setting-update.component.html',
})
export class ConfigSettingUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    sourceType: [null, [Validators.required, Validators.maxLength(20)]],
    mcUserId: [null, [Validators.maxLength(15)]],
    tmlUserId: [null, [Validators.maxLength(15)]],
    vmSysDefaultModeConf: [null, [Validators.required, Validators.maxLength(20)]],
    vmSysSyncCycleConf: [null, [Validators.required]],
    vmSysSyncCycleUnitConf: [null, [Validators.required]],
    vmSysTargetDisplayNameConf: [null, [Validators.required, Validators.maxLength(20)]],
    vmLiveDefaultModeConf: [null, [Validators.required]],
    vmLiveDefaultTimerangeConf: [null, [Validators.required]],
    vmLivePositionCycleConf: [null, [Validators.required]],
    vmLivePositionCycleUnitConf: [null, [Validators.required]],
    vmLiveTrackingAmplitudeConf: [null, [Validators.required]],
    vmLiveTrackingAmplitudeUnitConf: [null, [Validators.required]],
    sarSysSyncCycleConf: [null, [Validators.required]],
    sarSysSyncCycleUnitConf: [null, [Validators.required]],
    sarSysObjectDisplayName01Conf: [null, [Validators.required, Validators.maxLength(255)]],
    sarSysObjectDisplayName02Conf: [null, [Validators.required, Validators.maxLength(255)]],
    createDate: [null, [Validators.required]],
    createUid: [null, [Validators.required, Validators.maxLength(15)]],
    lastUpdate: [null, [Validators.required]],
    lastUpdateUid: [null, [Validators.required, Validators.maxLength(15)]],
  });

  constructor(protected configSettingService: ConfigSettingService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configSetting }) => {
      if (configSetting.id === undefined) {
        const today = dayjs().startOf('day');
        configSetting.createDate = today;
        configSetting.lastUpdate = today;
      }

      this.updateForm(configSetting);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const configSetting = this.createFromForm();
    if (configSetting.id !== undefined) {
      this.subscribeToSaveResponse(this.configSettingService.update(configSetting));
    } else {
      this.subscribeToSaveResponse(this.configSettingService.create(configSetting));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConfigSetting>>): void {
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

  protected updateForm(configSetting: IConfigSetting): void {
    this.editForm.patchValue({
      id: configSetting.id,
      sourceType: configSetting.sourceType,
      mcUserId: configSetting.mcUserId,
      tmlUserId: configSetting.tmlUserId,
      vmSysDefaultModeConf: configSetting.vmSysDefaultModeConf,
      vmSysSyncCycleConf: configSetting.vmSysSyncCycleConf,
      vmSysSyncCycleUnitConf: configSetting.vmSysSyncCycleUnitConf,
      vmSysTargetDisplayNameConf: configSetting.vmSysTargetDisplayNameConf,
      vmLiveDefaultModeConf: configSetting.vmLiveDefaultModeConf,
      vmLiveDefaultTimerangeConf: configSetting.vmLiveDefaultTimerangeConf,
      vmLivePositionCycleConf: configSetting.vmLivePositionCycleConf,
      vmLivePositionCycleUnitConf: configSetting.vmLivePositionCycleUnitConf,
      vmLiveTrackingAmplitudeConf: configSetting.vmLiveTrackingAmplitudeConf,
      vmLiveTrackingAmplitudeUnitConf: configSetting.vmLiveTrackingAmplitudeUnitConf,
      sarSysSyncCycleConf: configSetting.sarSysSyncCycleConf,
      sarSysSyncCycleUnitConf: configSetting.sarSysSyncCycleUnitConf,
      sarSysObjectDisplayName01Conf: configSetting.sarSysObjectDisplayName01Conf,
      sarSysObjectDisplayName02Conf: configSetting.sarSysObjectDisplayName02Conf,
      createDate: configSetting.createDate ? configSetting.createDate.format(DATE_TIME_FORMAT) : null,
      createUid: configSetting.createUid,
      lastUpdate: configSetting.lastUpdate ? configSetting.lastUpdate.format(DATE_TIME_FORMAT) : null,
      lastUpdateUid: configSetting.lastUpdateUid,
    });
  }

  protected createFromForm(): IConfigSetting {
    return {
      ...new ConfigSetting(),
      id: this.editForm.get(['id'])!.value,
      sourceType: this.editForm.get(['sourceType'])!.value,
      mcUserId: this.editForm.get(['mcUserId'])!.value,
      tmlUserId: this.editForm.get(['tmlUserId'])!.value,
      vmSysDefaultModeConf: this.editForm.get(['vmSysDefaultModeConf'])!.value,
      vmSysSyncCycleConf: this.editForm.get(['vmSysSyncCycleConf'])!.value,
      vmSysSyncCycleUnitConf: this.editForm.get(['vmSysSyncCycleUnitConf'])!.value,
      vmSysTargetDisplayNameConf: this.editForm.get(['vmSysTargetDisplayNameConf'])!.value,
      vmLiveDefaultModeConf: this.editForm.get(['vmLiveDefaultModeConf'])!.value,
      vmLiveDefaultTimerangeConf: this.editForm.get(['vmLiveDefaultTimerangeConf'])!.value,
      vmLivePositionCycleConf: this.editForm.get(['vmLivePositionCycleConf'])!.value,
      vmLivePositionCycleUnitConf: this.editForm.get(['vmLivePositionCycleUnitConf'])!.value,
      vmLiveTrackingAmplitudeConf: this.editForm.get(['vmLiveTrackingAmplitudeConf'])!.value,
      vmLiveTrackingAmplitudeUnitConf: this.editForm.get(['vmLiveTrackingAmplitudeUnitConf'])!.value,
      sarSysSyncCycleConf: this.editForm.get(['sarSysSyncCycleConf'])!.value,
      sarSysSyncCycleUnitConf: this.editForm.get(['sarSysSyncCycleUnitConf'])!.value,
      sarSysObjectDisplayName01Conf: this.editForm.get(['sarSysObjectDisplayName01Conf'])!.value,
      sarSysObjectDisplayName02Conf: this.editForm.get(['sarSysObjectDisplayName02Conf'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      createUid: this.editForm.get(['createUid'])!.value,
      lastUpdate: this.editForm.get(['lastUpdate'])!.value ? dayjs(this.editForm.get(['lastUpdate'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdateUid: this.editForm.get(['lastUpdateUid'])!.value,
    };
  }
}
