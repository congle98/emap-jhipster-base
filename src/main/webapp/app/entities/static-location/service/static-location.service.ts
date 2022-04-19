import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStaticLocation, getStaticLocationIdentifier } from '../static-location.model';

export type EntityResponseType = HttpResponse<IStaticLocation>;
export type EntityArrayResponseType = HttpResponse<IStaticLocation[]>;

@Injectable({ providedIn: 'root' })
export class StaticLocationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/static-locations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(staticLocation: IStaticLocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(staticLocation);
    return this.http
      .post<IStaticLocation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(staticLocation: IStaticLocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(staticLocation);
    return this.http
      .put<IStaticLocation>(`${this.resourceUrl}/${getStaticLocationIdentifier(staticLocation) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(staticLocation: IStaticLocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(staticLocation);
    return this.http
      .patch<IStaticLocation>(`${this.resourceUrl}/${getStaticLocationIdentifier(staticLocation) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IStaticLocation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IStaticLocation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStaticLocationToCollectionIfMissing(
    staticLocationCollection: IStaticLocation[],
    ...staticLocationsToCheck: (IStaticLocation | null | undefined)[]
  ): IStaticLocation[] {
    const staticLocations: IStaticLocation[] = staticLocationsToCheck.filter(isPresent);
    if (staticLocations.length > 0) {
      const staticLocationCollectionIdentifiers = staticLocationCollection.map(
        staticLocationItem => getStaticLocationIdentifier(staticLocationItem)!
      );
      const staticLocationsToAdd = staticLocations.filter(staticLocationItem => {
        const staticLocationIdentifier = getStaticLocationIdentifier(staticLocationItem);
        if (staticLocationIdentifier == null || staticLocationCollectionIdentifiers.includes(staticLocationIdentifier)) {
          return false;
        }
        staticLocationCollectionIdentifiers.push(staticLocationIdentifier);
        return true;
      });
      return [...staticLocationsToAdd, ...staticLocationCollection];
    }
    return staticLocationCollection;
  }

  protected convertDateFromClient(staticLocation: IStaticLocation): IStaticLocation {
    return Object.assign({}, staticLocation, {
      createDate: staticLocation.createDate?.isValid() ? staticLocation.createDate.toJSON() : undefined,
      lastUpdate: staticLocation.lastUpdate?.isValid() ? staticLocation.lastUpdate.toJSON() : undefined,
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
      res.body.forEach((staticLocation: IStaticLocation) => {
        staticLocation.createDate = staticLocation.createDate ? dayjs(staticLocation.createDate) : undefined;
        staticLocation.lastUpdate = staticLocation.lastUpdate ? dayjs(staticLocation.lastUpdate) : undefined;
      });
    }
    return res;
  }
}
