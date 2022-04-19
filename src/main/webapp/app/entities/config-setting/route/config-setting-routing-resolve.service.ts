import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConfigSetting, ConfigSetting } from '../config-setting.model';
import { ConfigSettingService } from '../service/config-setting.service';

@Injectable({ providedIn: 'root' })
export class ConfigSettingRoutingResolveService implements Resolve<IConfigSetting> {
  constructor(protected service: ConfigSettingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConfigSetting> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((configSetting: HttpResponse<ConfigSetting>) => {
          if (configSetting.body) {
            return of(configSetting.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ConfigSetting());
  }
}
