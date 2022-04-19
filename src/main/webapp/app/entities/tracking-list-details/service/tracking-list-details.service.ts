import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrackingListDetails, getTrackingListDetailsIdentifier } from '../tracking-list-details.model';

export type EntityResponseType = HttpResponse<ITrackingListDetails>;
export type EntityArrayResponseType = HttpResponse<ITrackingListDetails[]>;

@Injectable({ providedIn: 'root' })
export class TrackingListDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tracking-list-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(trackingListDetails: ITrackingListDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trackingListDetails);
    return this.http
      .post<ITrackingListDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(trackingListDetails: ITrackingListDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trackingListDetails);
    return this.http
      .put<ITrackingListDetails>(`${this.resourceUrl}/${getTrackingListDetailsIdentifier(trackingListDetails) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(trackingListDetails: ITrackingListDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trackingListDetails);
    return this.http
      .patch<ITrackingListDetails>(`${this.resourceUrl}/${getTrackingListDetailsIdentifier(trackingListDetails) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITrackingListDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITrackingListDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTrackingListDetailsToCollectionIfMissing(
    trackingListDetailsCollection: ITrackingListDetails[],
    ...trackingListDetailsToCheck: (ITrackingListDetails | null | undefined)[]
  ): ITrackingListDetails[] {
    const trackingListDetails: ITrackingListDetails[] = trackingListDetailsToCheck.filter(isPresent);
    if (trackingListDetails.length > 0) {
      const trackingListDetailsCollectionIdentifiers = trackingListDetailsCollection.map(
        trackingListDetailsItem => getTrackingListDetailsIdentifier(trackingListDetailsItem)!
      );
      const trackingListDetailsToAdd = trackingListDetails.filter(trackingListDetailsItem => {
        const trackingListDetailsIdentifier = getTrackingListDetailsIdentifier(trackingListDetailsItem);
        if (trackingListDetailsIdentifier == null || trackingListDetailsCollectionIdentifiers.includes(trackingListDetailsIdentifier)) {
          return false;
        }
        trackingListDetailsCollectionIdentifiers.push(trackingListDetailsIdentifier);
        return true;
      });
      return [...trackingListDetailsToAdd, ...trackingListDetailsCollection];
    }
    return trackingListDetailsCollection;
  }

  protected convertDateFromClient(trackingListDetails: ITrackingListDetails): ITrackingListDetails {
    return Object.assign({}, trackingListDetails, {
      createDate: trackingListDetails.createDate?.isValid() ? trackingListDetails.createDate.toJSON() : undefined,
      lastUpdate: trackingListDetails.lastUpdate?.isValid() ? trackingListDetails.lastUpdate.toJSON() : undefined,
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
      res.body.forEach((trackingListDetails: ITrackingListDetails) => {
        trackingListDetails.createDate = trackingListDetails.createDate ? dayjs(trackingListDetails.createDate) : undefined;
        trackingListDetails.lastUpdate = trackingListDetails.lastUpdate ? dayjs(trackingListDetails.lastUpdate) : undefined;
      });
    }
    return res;
  }
}
