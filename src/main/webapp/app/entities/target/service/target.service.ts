import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITarget, getTargetIdentifier } from '../target.model';

export type EntityResponseType = HttpResponse<ITarget>;
export type EntityArrayResponseType = HttpResponse<ITarget[]>;

@Injectable({ providedIn: 'root' })
export class TargetService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/targets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(target: ITarget): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(target);
    return this.http
      .post<ITarget>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(target: ITarget): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(target);
    return this.http
      .put<ITarget>(`${this.resourceUrl}/${getTargetIdentifier(target) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(target: ITarget): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(target);
    return this.http
      .patch<ITarget>(`${this.resourceUrl}/${getTargetIdentifier(target) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITarget>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITarget[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTargetToCollectionIfMissing(targetCollection: ITarget[], ...targetsToCheck: (ITarget | null | undefined)[]): ITarget[] {
    const targets: ITarget[] = targetsToCheck.filter(isPresent);
    if (targets.length > 0) {
      const targetCollectionIdentifiers = targetCollection.map(targetItem => getTargetIdentifier(targetItem)!);
      const targetsToAdd = targets.filter(targetItem => {
        const targetIdentifier = getTargetIdentifier(targetItem);
        if (targetIdentifier == null || targetCollectionIdentifiers.includes(targetIdentifier)) {
          return false;
        }
        targetCollectionIdentifiers.push(targetIdentifier);
        return true;
      });
      return [...targetsToAdd, ...targetCollection];
    }
    return targetCollection;
  }

  protected convertDateFromClient(target: ITarget): ITarget {
    return Object.assign({}, target, {
      createDate: target.createDate?.isValid() ? target.createDate.toJSON() : undefined,
      lastUpdate: target.lastUpdate?.isValid() ? target.lastUpdate.toJSON() : undefined,
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
      res.body.forEach((target: ITarget) => {
        target.createDate = target.createDate ? dayjs(target.createDate) : undefined;
        target.lastUpdate = target.lastUpdate ? dayjs(target.lastUpdate) : undefined;
      });
    }
    return res;
  }
}
