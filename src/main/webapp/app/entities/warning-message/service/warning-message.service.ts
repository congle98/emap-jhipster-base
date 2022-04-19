import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWarningMessage, getWarningMessageIdentifier } from '../warning-message.model';

export type EntityResponseType = HttpResponse<IWarningMessage>;
export type EntityArrayResponseType = HttpResponse<IWarningMessage[]>;

@Injectable({ providedIn: 'root' })
export class WarningMessageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/warning-messages');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(warningMessage: IWarningMessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(warningMessage);
    return this.http
      .post<IWarningMessage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(warningMessage: IWarningMessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(warningMessage);
    return this.http
      .put<IWarningMessage>(`${this.resourceUrl}/${getWarningMessageIdentifier(warningMessage) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(warningMessage: IWarningMessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(warningMessage);
    return this.http
      .patch<IWarningMessage>(`${this.resourceUrl}/${getWarningMessageIdentifier(warningMessage) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWarningMessage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWarningMessage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWarningMessageToCollectionIfMissing(
    warningMessageCollection: IWarningMessage[],
    ...warningMessagesToCheck: (IWarningMessage | null | undefined)[]
  ): IWarningMessage[] {
    const warningMessages: IWarningMessage[] = warningMessagesToCheck.filter(isPresent);
    if (warningMessages.length > 0) {
      const warningMessageCollectionIdentifiers = warningMessageCollection.map(
        warningMessageItem => getWarningMessageIdentifier(warningMessageItem)!
      );
      const warningMessagesToAdd = warningMessages.filter(warningMessageItem => {
        const warningMessageIdentifier = getWarningMessageIdentifier(warningMessageItem);
        if (warningMessageIdentifier == null || warningMessageCollectionIdentifiers.includes(warningMessageIdentifier)) {
          return false;
        }
        warningMessageCollectionIdentifiers.push(warningMessageIdentifier);
        return true;
      });
      return [...warningMessagesToAdd, ...warningMessageCollection];
    }
    return warningMessageCollection;
  }

  protected convertDateFromClient(warningMessage: IWarningMessage): IWarningMessage {
    return Object.assign({}, warningMessage, {
      createDate: warningMessage.createDate?.isValid() ? warningMessage.createDate.toJSON() : undefined,
      lastUpdate: warningMessage.lastUpdate?.isValid() ? warningMessage.lastUpdate.toJSON() : undefined,
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
      res.body.forEach((warningMessage: IWarningMessage) => {
        warningMessage.createDate = warningMessage.createDate ? dayjs(warningMessage.createDate) : undefined;
        warningMessage.lastUpdate = warningMessage.lastUpdate ? dayjs(warningMessage.lastUpdate) : undefined;
      });
    }
    return res;
  }
}
