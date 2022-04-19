import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrackingList, getTrackingListIdentifier } from '../tracking-list.model';

export type EntityResponseType = HttpResponse<ITrackingList>;
export type EntityArrayResponseType = HttpResponse<ITrackingList[]>;

@Injectable({ providedIn: 'root' })
export class TrackingListService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tracking-lists');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(trackingList: ITrackingList): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trackingList);
    return this.http
      .post<ITrackingList>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(trackingList: ITrackingList): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trackingList);
    return this.http
      .put<ITrackingList>(`${this.resourceUrl}/${getTrackingListIdentifier(trackingList) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(trackingList: ITrackingList): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trackingList);
    return this.http
      .patch<ITrackingList>(`${this.resourceUrl}/${getTrackingListIdentifier(trackingList) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITrackingList>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITrackingList[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTrackingListToCollectionIfMissing(
    trackingListCollection: ITrackingList[],
    ...trackingListsToCheck: (ITrackingList | null | undefined)[]
  ): ITrackingList[] {
    const trackingLists: ITrackingList[] = trackingListsToCheck.filter(isPresent);
    if (trackingLists.length > 0) {
      const trackingListCollectionIdentifiers = trackingListCollection.map(
        trackingListItem => getTrackingListIdentifier(trackingListItem)!
      );
      const trackingListsToAdd = trackingLists.filter(trackingListItem => {
        const trackingListIdentifier = getTrackingListIdentifier(trackingListItem);
        if (trackingListIdentifier == null || trackingListCollectionIdentifiers.includes(trackingListIdentifier)) {
          return false;
        }
        trackingListCollectionIdentifiers.push(trackingListIdentifier);
        return true;
      });
      return [...trackingListsToAdd, ...trackingListCollection];
    }
    return trackingListCollection;
  }

  protected convertDateFromClient(trackingList: ITrackingList): ITrackingList {
    return Object.assign({}, trackingList, {
      createDate: trackingList.createDate?.isValid() ? trackingList.createDate.toJSON() : undefined,
      lastUpdate: trackingList.lastUpdate?.isValid() ? trackingList.lastUpdate.toJSON() : undefined,
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
      res.body.forEach((trackingList: ITrackingList) => {
        trackingList.createDate = trackingList.createDate ? dayjs(trackingList.createDate) : undefined;
        trackingList.lastUpdate = trackingList.lastUpdate ? dayjs(trackingList.lastUpdate) : undefined;
      });
    }
    return res;
  }
}
