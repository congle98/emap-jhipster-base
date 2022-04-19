import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IWarningRule, WarningRule } from '../warning-rule.model';

import { WarningRuleService } from './warning-rule.service';

describe('WarningRule Service', () => {
  let service: WarningRuleService;
  let httpMock: HttpTestingController;
  let elemDefault: IWarningRule;
  let expectedResult: IWarningRule | IWarningRule[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WarningRuleService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      mcUserId: 'AAAAAAA',
      delayCheck: 0,
      delayCheckUnit: 'AAAAAAA',
      conditionType: 'AAAAAAA',
      includeMcCampaignId: 'AAAAAAA',
      includeMcTargetId: 'AAAAAAA',
      warningDistance: 0,
      showWarningCircle: false,
      showWarningMessage: false,
      warningMessage: 'AAAAAAA',
      sendWarningMessageToMc: false,
      status: false,
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

    it('should create a WarningRule', () => {
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

      service.create(new WarningRule()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WarningRule', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          mcUserId: 'BBBBBB',
          delayCheck: 1,
          delayCheckUnit: 'BBBBBB',
          conditionType: 'BBBBBB',
          includeMcCampaignId: 'BBBBBB',
          includeMcTargetId: 'BBBBBB',
          warningDistance: 1,
          showWarningCircle: true,
          showWarningMessage: true,
          warningMessage: 'BBBBBB',
          sendWarningMessageToMc: true,
          status: true,
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

    it('should partial update a WarningRule', () => {
      const patchObject = Object.assign(
        {
          mcUserId: 'BBBBBB',
          includeMcCampaignId: 'BBBBBB',
          includeMcTargetId: 'BBBBBB',
          showWarningCircle: true,
          showWarningMessage: true,
          warningMessage: 'BBBBBB',
          sendWarningMessageToMc: true,
          status: true,
          createUid: 'BBBBBB',
        },
        new WarningRule()
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

    it('should return a list of WarningRule', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          mcUserId: 'BBBBBB',
          delayCheck: 1,
          delayCheckUnit: 'BBBBBB',
          conditionType: 'BBBBBB',
          includeMcCampaignId: 'BBBBBB',
          includeMcTargetId: 'BBBBBB',
          warningDistance: 1,
          showWarningCircle: true,
          showWarningMessage: true,
          warningMessage: 'BBBBBB',
          sendWarningMessageToMc: true,
          status: true,
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

    it('should delete a WarningRule', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addWarningRuleToCollectionIfMissing', () => {
      it('should add a WarningRule to an empty array', () => {
        const warningRule: IWarningRule = { id: 123 };
        expectedResult = service.addWarningRuleToCollectionIfMissing([], warningRule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(warningRule);
      });

      it('should not add a WarningRule to an array that contains it', () => {
        const warningRule: IWarningRule = { id: 123 };
        const warningRuleCollection: IWarningRule[] = [
          {
            ...warningRule,
          },
          { id: 456 },
        ];
        expectedResult = service.addWarningRuleToCollectionIfMissing(warningRuleCollection, warningRule);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WarningRule to an array that doesn't contain it", () => {
        const warningRule: IWarningRule = { id: 123 };
        const warningRuleCollection: IWarningRule[] = [{ id: 456 }];
        expectedResult = service.addWarningRuleToCollectionIfMissing(warningRuleCollection, warningRule);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(warningRule);
      });

      it('should add only unique WarningRule to an array', () => {
        const warningRuleArray: IWarningRule[] = [{ id: 123 }, { id: 456 }, { id: 47669 }];
        const warningRuleCollection: IWarningRule[] = [{ id: 123 }];
        expectedResult = service.addWarningRuleToCollectionIfMissing(warningRuleCollection, ...warningRuleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const warningRule: IWarningRule = { id: 123 };
        const warningRule2: IWarningRule = { id: 456 };
        expectedResult = service.addWarningRuleToCollectionIfMissing([], warningRule, warningRule2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(warningRule);
        expect(expectedResult).toContain(warningRule2);
      });

      it('should accept null and undefined values', () => {
        const warningRule: IWarningRule = { id: 123 };
        expectedResult = service.addWarningRuleToCollectionIfMissing([], null, warningRule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(warningRule);
      });

      it('should return initial array if no WarningRule is added', () => {
        const warningRuleCollection: IWarningRule[] = [{ id: 123 }];
        expectedResult = service.addWarningRuleToCollectionIfMissing(warningRuleCollection, undefined, null);
        expect(expectedResult).toEqual(warningRuleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
