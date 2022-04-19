import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICoordinatesDetails, getCoordinatesDetailsIdentifier } from '../coordinates-details.model';

export type EntityResponseType = HttpResponse<ICoordinatesDetails>;
export type EntityArrayResponseType = HttpResponse<ICoordinatesDetails[]>;

@Injectable({ providedIn: 'root' })
export class CoordinatesDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/coordinates-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(coordinatesDetails: ICoordinatesDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(coordinatesDetails);
    return this.http
      .post<ICoordinatesDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(coordinatesDetails: ICoordinatesDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(coordinatesDetails);
    return this.http
      .put<ICoordinatesDetails>(`${this.resourceUrl}/${getCoordinatesDetailsIdentifier(coordinatesDetails) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(coordinatesDetails: ICoordinatesDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(coordinatesDetails);
    return this.http
      .patch<ICoordinatesDetails>(`${this.resourceUrl}/${getCoordinatesDetailsIdentifier(coordinatesDetails) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICoordinatesDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICoordinatesDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCoordinatesDetailsToCollectionIfMissing(
    coordinatesDetailsCollection: ICoordinatesDetails[],
    ...coordinatesDetailsToCheck: (ICoordinatesDetails | null | undefined)[]
  ): ICoordinatesDetails[] {
    const coordinatesDetails: ICoordinatesDetails[] = coordinatesDetailsToCheck.filter(isPresent);
    if (coordinatesDetails.length > 0) {
      const coordinatesDetailsCollectionIdentifiers = coordinatesDetailsCollection.map(
        coordinatesDetailsItem => getCoordinatesDetailsIdentifier(coordinatesDetailsItem)!
      );
      const coordinatesDetailsToAdd = coordinatesDetails.filter(coordinatesDetailsItem => {
        const coordinatesDetailsIdentifier = getCoordinatesDetailsIdentifier(coordinatesDetailsItem);
        if (coordinatesDetailsIdentifier == null || coordinatesDetailsCollectionIdentifiers.includes(coordinatesDetailsIdentifier)) {
          return false;
        }
        coordinatesDetailsCollectionIdentifiers.push(coordinatesDetailsIdentifier);
        return true;
      });
      return [...coordinatesDetailsToAdd, ...coordinatesDetailsCollection];
    }
    return coordinatesDetailsCollection;
  }

  protected convertDateFromClient(coordinatesDetails: ICoordinatesDetails): ICoordinatesDetails {
    return Object.assign({}, coordinatesDetails, {
      createDate: coordinatesDetails.createDate?.isValid() ? coordinatesDetails.createDate.toJSON() : undefined,
      lastUpdate: coordinatesDetails.lastUpdate?.isValid() ? coordinatesDetails.lastUpdate.toJSON() : undefined,
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
      res.body.forEach((coordinatesDetails: ICoordinatesDetails) => {
        coordinatesDetails.createDate = coordinatesDetails.createDate ? dayjs(coordinatesDetails.createDate) : undefined;
        coordinatesDetails.lastUpdate = coordinatesDetails.lastUpdate ? dayjs(coordinatesDetails.lastUpdate) : undefined;
      });
    }
    return res;
  }
}
