import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConfigSetting } from '../config-setting.model';

@Component({
  selector: 'jhi-config-setting-detail',
  templateUrl: './config-setting-detail.component.html',
})
export class ConfigSettingDetailComponent implements OnInit {
  configSetting: IConfigSetting | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configSetting }) => {
      this.configSetting = configSetting;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
