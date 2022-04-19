import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IWarningMessage, WarningMessage } from '../warning-message.model';

import { WarningMessageService } from './warning-message.service';

describe('WarningMessage Service', () => {
  let service: WarningMessageService;
  let httpMock: HttpTestingController;
  let elemDefault: IWarningMessage;
  let expectedResult: IWarningMessage | IWarningMessage[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WarningMessageService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      mcUserId: 'AAAAAAA',
      warningDistance: 'AAAAAAA',
      showWarningCircle: false,
      showWarningMessage: false,
      warningMessage: 'AAAAAAA',
      sendWarningMessageToMc: false,
      createDate: currentDate,
      createUid: 'AAAAAAA',
      lastUpdate: currentDate,
      lastUpdateUid: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          createDate: currentDate.format(DATE_TIME_FORMAT),
          lastUpdate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a WarningMessage', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          createDate: currentDate.format(DATE_TIME_FORMAT),
          lastUpdate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createDate: currentDate,
          lastUpdate: currentDate,
        },
        returnedFromService
      );

      service.create(new WarningMessage()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WarningMessage', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          mcUserId: 'BBBBBB',
          warningDistance: 'BBBBBB',
          showWarningCircle: true,
          showWarningMessage: true,
          warningMessage: 'BBBBBB',
          sendWarningMessageToMc: true,
          createDate: currentDate.format(DATE_TIME_FORMAT),
          createUid: 'BBBBBB',
          lastUpdate: currentDate.format(DATE_TIME_FORMAT),
          lastUpdateUid: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createDate: currentDate,
          lastUpdate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WarningMessage', () => {
      const patchObject = Object.assign(
        {
          mcUserId: 'BBBBBB',
          warningDistance: 'BBBBBB',
          showWarningCircle: true,
          showWarningMessage: true,
          createDate: currentDate.format(DATE_TIME_FORMAT),
          lastUpdateUid: 'BBBBBB',
        },
        new WarningMessage()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          createDate: currentDate,
          lastUpdate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WarningMessage', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          mcUserId: 'BBBBBB',
          warningDistance: 'BBBBBB',
          showWarningCircle: true,
          showWarningMessage: true,
          warningMessage: 'BBBBBB',
          sendWarningMessageToMc: true,
          createDate: currentDate.format(DATE_TIME_FORMAT),
          createUid: 'BBBBBB',
          lastUpdate: currentDate.format(DATE_TIME_FORMAT),
          lastUpdateUid: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createDate: currentDate,
          lastUpdate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a WarningMessage', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addWarningMessageToCollectionIfMissing', () => {
      it('should add a WarningMessage to an empty array', () => {
        const warningMessage: IWarningMessage = { id: 123 };
        expectedResult = service.addWarningMessageToCollectionIfMissing([], warningMessage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(warningMessage);
      });

      it('should not add a WarningMessage to an array that contains it', () => {
        const warningMessage: IWarningMessage = { id: 123 };
        const warningMessageCollection: IWarningMessage[] = [
          {
            ...warningMessage,
          },
          { id: 456 },
        ];
        expectedResult = service.addWarningMessageToCollectionIfMissing(warningMessageCollection, warningMessage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WarningMessage to an array that doesn't contain it", () => {
        const warningMessage: IWarningMessage = { id: 123 };
        const warningMessageCollection: IWarningMessage[] = [{ id: 456 }];
        expectedResult = service.addWarningMessageToCollectionIfMissing(warningMessageCollection, warningMessage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(warningMessage);
      });

      it('should add only unique WarningMessage to an array', () => {
        const warningMessageArray: IWarningMessage[] = [{ id: 123 }, { id: 456 }, { id: 63344 }];
        const warningMessageCollection: IWarningMessage[] = [{ id: 123 }];
        expectedResult = service.addWarningMessageToCollectionIfMissing(warningMessageCollection, ...warningMessageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const warningMessage: IWarningMessage = { id: 123 };
        const warningMessage2: IWarningMessage = { id: 456 };
        expectedResult = service.addWarningMessageToCollectionIfMissing([], warningMessage, warningMessage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(warningMessage);
        expect(expectedResult).toContain(warningMessage2);
      });

      it('should accept null and undefined values', () => {
        const warningMessage: IWarningMessage = { id: 123 };
        expectedResult = service.addWarningMessageToCollectionIfMissing([], null, warningMessage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(warningMessage);
      });

      it('should return initial array if no WarningMessage is added', () => {
        const warningMessageCollection: IWarningMessage[] = [{ id: 123 }];
        expectedResult = service.addWarningMessageToCollectionIfMissing(warningMessageCollection, undefined, null);
        expect(expectedResult).toEqual(warningMessageCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
