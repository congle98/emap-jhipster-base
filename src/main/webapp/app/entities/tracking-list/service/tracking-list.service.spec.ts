import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITrackingList, TrackingList } from '../tracking-list.model';

import { TrackingListService } from './tracking-list.service';

describe('TrackingList Service', () => {
  let service: TrackingListService;
  let httpMock: HttpTestingController;
  let elemDefault: ITrackingList;
  let expectedResult: ITrackingList | ITrackingList[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TrackingListService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      mcUserId: 'AAAAAAA',
      type: 'AAAAAAA',
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

    it('should create a TrackingList', () => {
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

      service.create(new TrackingList()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TrackingList', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          mcUserId: 'BBBBBB',
          type: 'BBBBBB',
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

    it('should partial update a TrackingList', () => {
      const patchObject = Object.assign(
        {
          type: 'BBBBBB',
          createDate: currentDate.format(DATE_TIME_FORMAT),
          lastUpdate: currentDate.format(DATE_TIME_FORMAT),
        },
        new TrackingList()
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

    it('should return a list of TrackingList', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          mcUserId: 'BBBBBB',
          type: 'BBBBBB',
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

    it('should delete a TrackingList', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTrackingListToCollectionIfMissing', () => {
      it('should add a TrackingList to an empty array', () => {
        const trackingList: ITrackingList = { id: 123 };
        expectedResult = service.addTrackingListToCollectionIfMissing([], trackingList);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trackingList);
      });

      it('should not add a TrackingList to an array that contains it', () => {
        const trackingList: ITrackingList = { id: 123 };
        const trackingListCollection: ITrackingList[] = [
          {
            ...trackingList,
          },
          { id: 456 },
        ];
        expectedResult = service.addTrackingListToCollectionIfMissing(trackingListCollection, trackingList);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TrackingList to an array that doesn't contain it", () => {
        const trackingList: ITrackingList = { id: 123 };
        const trackingListCollection: ITrackingList[] = [{ id: 456 }];
        expectedResult = service.addTrackingListToCollectionIfMissing(trackingListCollection, trackingList);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trackingList);
      });

      it('should add only unique TrackingList to an array', () => {
        const trackingListArray: ITrackingList[] = [{ id: 123 }, { id: 456 }, { id: 61641 }];
        const trackingListCollection: ITrackingList[] = [{ id: 123 }];
        expectedResult = service.addTrackingListToCollectionIfMissing(trackingListCollection, ...trackingListArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trackingList: ITrackingList = { id: 123 };
        const trackingList2: ITrackingList = { id: 456 };
        expectedResult = service.addTrackingListToCollectionIfMissing([], trackingList, trackingList2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trackingList);
        expect(expectedResult).toContain(trackingList2);
      });

      it('should accept null and undefined values', () => {
        const trackingList: ITrackingList = { id: 123 };
        expectedResult = service.addTrackingListToCollectionIfMissing([], null, trackingList, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trackingList);
      });

      it('should return initial array if no TrackingList is added', () => {
        const trackingListCollection: ITrackingList[] = [{ id: 123 }];
        expectedResult = service.addTrackingListToCollectionIfMissing(trackingListCollection, undefined, null);
        expect(expectedResult).toEqual(trackingListCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
