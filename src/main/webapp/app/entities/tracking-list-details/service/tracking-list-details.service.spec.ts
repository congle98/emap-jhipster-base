import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITrackingListDetails, TrackingListDetails } from '../tracking-list-details.model';

import { TrackingListDetailsService } from './tracking-list-details.service';

describe('TrackingListDetails Service', () => {
  let service: TrackingListDetailsService;
  let httpMock: HttpTestingController;
  let elemDefault: ITrackingListDetails;
  let expectedResult: ITrackingListDetails | ITrackingListDetails[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TrackingListDetailsService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
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

    it('should create a TrackingListDetails', () => {
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

      service.create(new TrackingListDetails()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TrackingListDetails', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should partial update a TrackingListDetails', () => {
      const patchObject = Object.assign(
        {
          createDate: currentDate.format(DATE_TIME_FORMAT),
          lastUpdate: currentDate.format(DATE_TIME_FORMAT),
          lastUpdateUid: 'BBBBBB',
        },
        new TrackingListDetails()
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

    it('should return a list of TrackingListDetails', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should delete a TrackingListDetails', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTrackingListDetailsToCollectionIfMissing', () => {
      it('should add a TrackingListDetails to an empty array', () => {
        const trackingListDetails: ITrackingListDetails = { id: 123 };
        expectedResult = service.addTrackingListDetailsToCollectionIfMissing([], trackingListDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trackingListDetails);
      });

      it('should not add a TrackingListDetails to an array that contains it', () => {
        const trackingListDetails: ITrackingListDetails = { id: 123 };
        const trackingListDetailsCollection: ITrackingListDetails[] = [
          {
            ...trackingListDetails,
          },
          { id: 456 },
        ];
        expectedResult = service.addTrackingListDetailsToCollectionIfMissing(trackingListDetailsCollection, trackingListDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TrackingListDetails to an array that doesn't contain it", () => {
        const trackingListDetails: ITrackingListDetails = { id: 123 };
        const trackingListDetailsCollection: ITrackingListDetails[] = [{ id: 456 }];
        expectedResult = service.addTrackingListDetailsToCollectionIfMissing(trackingListDetailsCollection, trackingListDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trackingListDetails);
      });

      it('should add only unique TrackingListDetails to an array', () => {
        const trackingListDetailsArray: ITrackingListDetails[] = [{ id: 123 }, { id: 456 }, { id: 6260 }];
        const trackingListDetailsCollection: ITrackingListDetails[] = [{ id: 123 }];
        expectedResult = service.addTrackingListDetailsToCollectionIfMissing(trackingListDetailsCollection, ...trackingListDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trackingListDetails: ITrackingListDetails = { id: 123 };
        const trackingListDetails2: ITrackingListDetails = { id: 456 };
        expectedResult = service.addTrackingListDetailsToCollectionIfMissing([], trackingListDetails, trackingListDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trackingListDetails);
        expect(expectedResult).toContain(trackingListDetails2);
      });

      it('should accept null and undefined values', () => {
        const trackingListDetails: ITrackingListDetails = { id: 123 };
        expectedResult = service.addTrackingListDetailsToCollectionIfMissing([], null, trackingListDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trackingListDetails);
      });

      it('should return initial array if no TrackingListDetails is added', () => {
        const trackingListDetailsCollection: ITrackingListDetails[] = [{ id: 123 }];
        expectedResult = service.addTrackingListDetailsToCollectionIfMissing(trackingListDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(trackingListDetailsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
