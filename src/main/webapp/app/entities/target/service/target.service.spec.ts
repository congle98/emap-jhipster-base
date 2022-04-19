import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITarget, Target } from '../target.model';

import { TargetService } from './target.service';

describe('Target Service', () => {
  let service: TargetService;
  let httpMock: HttpTestingController;
  let elemDefault: ITarget;
  let expectedResult: ITarget | ITarget[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TargetService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      sourceType: 'AAAAAAA',
      mcCampaingnId: 'AAAAAAA',
      tmlCampaignId: 'AAAAAAA',
      icon: 'AAAAAAA',
      color: 'AAAAAAA',
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

    it('should create a Target', () => {
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

      service.create(new Target()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Target', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          sourceType: 'BBBBBB',
          mcCampaingnId: 'BBBBBB',
          tmlCampaignId: 'BBBBBB',
          icon: 'BBBBBB',
          color: 'BBBBBB',
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

    it('should partial update a Target', () => {
      const patchObject = Object.assign(
        {
          tmlCampaignId: 'BBBBBB',
          color: 'BBBBBB',
          lastUpdate: currentDate.format(DATE_TIME_FORMAT),
        },
        new Target()
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

    it('should return a list of Target', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          sourceType: 'BBBBBB',
          mcCampaingnId: 'BBBBBB',
          tmlCampaignId: 'BBBBBB',
          icon: 'BBBBBB',
          color: 'BBBBBB',
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

    it('should delete a Target', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTargetToCollectionIfMissing', () => {
      it('should add a Target to an empty array', () => {
        const target: ITarget = { id: 123 };
        expectedResult = service.addTargetToCollectionIfMissing([], target);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(target);
      });

      it('should not add a Target to an array that contains it', () => {
        const target: ITarget = { id: 123 };
        const targetCollection: ITarget[] = [
          {
            ...target,
          },
          { id: 456 },
        ];
        expectedResult = service.addTargetToCollectionIfMissing(targetCollection, target);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Target to an array that doesn't contain it", () => {
        const target: ITarget = { id: 123 };
        const targetCollection: ITarget[] = [{ id: 456 }];
        expectedResult = service.addTargetToCollectionIfMissing(targetCollection, target);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(target);
      });

      it('should add only unique Target to an array', () => {
        const targetArray: ITarget[] = [{ id: 123 }, { id: 456 }, { id: 47847 }];
        const targetCollection: ITarget[] = [{ id: 123 }];
        expectedResult = service.addTargetToCollectionIfMissing(targetCollection, ...targetArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const target: ITarget = { id: 123 };
        const target2: ITarget = { id: 456 };
        expectedResult = service.addTargetToCollectionIfMissing([], target, target2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(target);
        expect(expectedResult).toContain(target2);
      });

      it('should accept null and undefined values', () => {
        const target: ITarget = { id: 123 };
        expectedResult = service.addTargetToCollectionIfMissing([], null, target, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(target);
      });

      it('should return initial array if no Target is added', () => {
        const targetCollection: ITarget[] = [{ id: 123 }];
        expectedResult = service.addTargetToCollectionIfMissing(targetCollection, undefined, null);
        expect(expectedResult).toEqual(targetCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
