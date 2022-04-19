import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConfigSetting, getConfigSettingIdentifier } from '../config-setting.model';

export type EntityResponseType = HttpResponse<IConfigSetting>;
export type EntityArrayResponseType = HttpResponse<IConfigSetting[]>;

@Injectable({ providedIn: 'root' })
export class ConfigSettingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/config-settings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(configSetting: IConfigSetting): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(configSetting);
    return this.http
      .post<IConfigSetting>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(configSetting: IConfigSetting): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(configSetting);
    return this.http
      .put<IConfigSetting>(`${this.resourceUrl}/${getConfigSettingIdentifier(configSetting) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(configSetting: IConfigSetting): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(configSetting);
    return this.http
      .patch<IConfigSetting>(`${this.resourceUrl}/${getConfigSettingIdentifier(configSetting) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IConfigSetting>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IConfigSetting[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addConfigSettingToCollectionIfMissing(
    configSettingCollection: IConfigSetting[],
    ...configSettingsToCheck: (IConfigSetting | null | undefined)[]
  ): IConfigSetting[] {
    const configSettings: IConfigSetting[] = configSettingsToCheck.filter(isPresent);
    if (configSettings.length > 0) {
      const configSettingCollectionIdentifiers = configSettingCollection.map(
        configSettingItem => getConfigSettingIdentifier(configSettingItem)!
      );
      const configSettingsToAdd = configSettings.filter(configSettingItem => {
        const configSettingIdentifier = getConfigSettingIdentifier(configSettingItem);
        if (configSettingIdentifier == null || configSettingCollectionIdentifiers.includes(configSettingIdentifier)) {
          return false;
        }
        configSettingCollectionIdentifiers.push(configSettingIdentifier);
        return true;
      });
      return [...configSettingsToAdd, ...configSettingCollection];
    }
    return configSettingCollection;
  }

  protected convertDateFromClient(configSetting: IConfigSetting): IConfigSetting {
    return Object.assign({}, configSetting, {
      createDate: configSetting.createDate?.isValid() ? configSetting.createDate.toJSON() : undefined,
      lastUpdate: configSetting.lastUpdate?.isValid() ? configSetting.lastUpdate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createDate = res.body.createDate ? dayjs(res.body.createDate) : undefined;
      res.body.lastUpdate = res.body.lastUpdate ? dayjs(res.body.lastUpdate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((configSetting: IConfigSetting) => {
        configSetting.createDate = configSetting.createDate ? dayjs(configSetting.createDate) : undefined;
        configSetting.lastUpdate = configSetting.lastUpdate ? dayjs(configSetting.lastUpdate) : undefined;
      });
    }
    return res;
  }
}
