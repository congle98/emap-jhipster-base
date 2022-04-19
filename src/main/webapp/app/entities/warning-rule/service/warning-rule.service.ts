import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWarningRule, getWarningRuleIdentifier } from '../warning-rule.model';

export type EntityResponseType = HttpResponse<IWarningRule>;
export type EntityArrayResponseType = HttpResponse<IWarningRule[]>;

@Injectable({ providedIn: 'root' })
export class WarningRuleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/warning-rules');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(warningRule: IWarningRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(warningRule);
    return this.http
      .post<IWarningRule>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(warningRule: IWarningRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(warningRule);
    return this.http
      .put<IWarningRule>(`${this.resourceUrl}/${getWarningRuleIdentifier(warningRule) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(warningRule: IWarningRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(warningRule);
    return this.http
      .patch<IWarningRule>(`${this.resourceUrl}/${getWarningRuleIdentifier(warningRule) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWarningRule>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWarningRule[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWarningRuleToCollectionIfMissing(
    warningRuleCollection: IWarningRule[],
    ...warningRulesToCheck: (IWarningRule | null | undefined)[]
  ): IWarningRule[] {
    const warningRules: IWarningRule[] = warningRulesToCheck.filter(isPresent);
    if (warningRules.length > 0) {
      const warningRuleCollectionIdentifiers = warningRuleCollection.map(warningRuleItem => getWarningRuleIdentifier(warningRuleItem)!);
      const warningRulesToAdd = warningRules.filter(warningRuleItem => {
        const warningRuleIdentifier = getWarningRuleIdentifier(warningRuleItem);
        if (warningRuleIdentifier == null || warningRuleCollectionIdentifiers.includes(warningRuleIdentifier)) {
          return false;
        }
        warningRuleCollectionIdentifiers.push(warningRuleIdentifier);
        return true;
      });
      return [...warningRulesToAdd, ...warningRuleCollection];
    }
    return warningRuleCollection;
  }

  protected convertDateFromClient(warningRule: IWarningRule): IWarningRule {
    return Object.assign({}, warningRule, {
      createDate: warningRule.createDate?.isValid() ? warningRule.createDate.toJSON() : undefined,
      lastUpdate: warningRule.lastUpdate?.isValid() ? warningRule.lastUpdate.toJSON() : undefined,
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
      res.body.forEach((warningRule: IWarningRule) => {
        warningRule.createDate = warningRule.createDate ? dayjs(warningRule.createDate) : undefined;
        warningRule.lastUpdate = warningRule.lastUpdate ? dayjs(warningRule.lastUpdate) : undefined;
      });
    }
    return res;
  }
}
