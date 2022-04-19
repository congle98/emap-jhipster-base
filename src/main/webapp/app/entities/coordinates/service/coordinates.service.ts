import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICoordinates, getCoordinatesIdentifier } from '../coordinates.model';

export type EntityResponseType = HttpResponse<ICoordinates>;
export type EntityArrayResponseType = HttpResponse<ICoordinates[]>;

@Injectable({ providedIn: 'root' })
export class CoordinatesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/coordinates');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(coordinates: ICoordinates): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(coordinates);
    return this.http
      .post<ICoordinates>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(coordinates: ICoordinates): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(coordinates);
    return this.http
      .put<ICoordinates>(`${this.resourceUrl}/${getCoordinatesIdentifier(coordinates) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(coordinates: ICoordinates): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(coordinates);
    return this.http
      .patch<ICoordinates>(`${this.resourceUrl}/${getCoordinatesIdentifier(coordinates) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICoordinates>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICoordinates[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCoordinatesToCollectionIfMissing(
    coordinatesCollection: ICoordinates[],
    ...coordinatesToCheck: (ICoordinates | null | undefined)[]
  ): ICoordinates[] {
    const coordinates: ICoordinates[] = coordinatesToCheck.filter(isPresent);
    if (coordinates.length > 0) {
      const coordinatesCollectionIdentifiers = coordinatesCollection.map(coordinatesItem => getCoordinatesIdentifier(coordinatesItem)!);
      const coordinatesToAdd = coordinates.filter(coordinatesItem => {
        const coordinatesIdentifier = getCoordinatesIdentifier(coordinatesItem);
        if (coordinatesIdentifier == null || coordinatesCollectionIdentifiers.includes(coordinatesIdentifier)) {
          return false;
        }
        coordinatesCollectionIdentifiers.push(coordinatesIdentifier);
        return true;
      });
      return [...coordinatesToAdd, ...coordinatesCollection];
    }
    return coordinatesCollection;
  }

  protected convertDateFromClient(coordinates: ICoordinates): ICoordinates {
    return Object.assign({}, coordinates, {
      createDate: coordinates.createDate?.isValid() ? coordinates.createDate.toJSON() : undefined,
      lastUpdate: coordinates.lastUpdate?.isValid() ? coordinates.lastUpdate.toJSON() : undefined,
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
      res.body.forEach((coordinates: ICoordinates) => {
        coordinates.createDate = coordinates.createDate ? dayjs(coordinates.createDate) : undefined;
        coordinates.lastUpdate = coordinates.lastUpdate ? dayjs(coordinates.lastUpdate) : undefined;
      });
    }
    return res;
  }
}
