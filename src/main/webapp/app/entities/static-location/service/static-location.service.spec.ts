import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IStaticLocation, StaticLocation } from '../static-location.model';

import { StaticLocationService } from './static-location.service';

describe('StaticLocation Service', () => {
  let service: StaticLocationService;
  let httpMock: HttpTestingController;
  let elemDefault: IStaticLocation;
  let expectedResult: IStaticLocation | IStaticLocation[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StaticLocationService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      mcUserId: 'AAAAAAA',
      address: 'AAAAAAA',
      lat: 'AAAAAAA',
      lng: 'AAAAAAA',
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

    it('should create a StaticLocation', () => {
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

      service.create(new StaticLocation()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StaticLocation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          mcUserId: 'BBBBBB',
          address: 'BBBBBB',
          lat: 'BBBBBB',
          lng: 'BBBBBB',
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

    it('should partial update a StaticLocation', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new StaticLocation()
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

    it('should return a list of StaticLocation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          mcUserId: 'BBBBBB',
          address: 'BBBBBB',
          lat: 'BBBBBB',
          lng: 'BBBBBB',
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

    it('should delete a StaticLocation', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addStaticLocationToCollectionIfMissing', () => {
      it('should add a StaticLocation to an empty array', () => {
        const staticLocation: IStaticLocation = { id: 123 };
        expectedResult = service.addStaticLocationToCollectionIfMissing([], staticLocation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(staticLocation);
      });

      it('should not add a StaticLocation to an array that contains it', () => {
        const staticLocation: IStaticLocation = { id: 123 };
        const staticLocationCollection: IStaticLocation[] = [
          {
            ...staticLocation,
          },
          { id: 456 },
        ];
        expectedResult = service.addStaticLocationToCollectionIfMissing(staticLocationCollection, staticLocation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StaticLocation to an array that doesn't contain it", () => {
        const staticLocation: IStaticLocation = { id: 123 };
        const staticLocationCollection: IStaticLocation[] = [{ id: 456 }];
        expectedResult = service.addStaticLocationToCollectionIfMissing(staticLocationCollection, staticLocation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(staticLocation);
      });

      it('should add only unique StaticLocation to an array', () => {
        const staticLocationArray: IStaticLocation[] = [{ id: 123 }, { id: 456 }, { id: 69997 }];
        const staticLocationCollection: IStaticLocation[] = [{ id: 123 }];
        expectedResult = service.addStaticLocationToCollectionIfMissing(staticLocationCollection, ...staticLocationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const staticLocation: IStaticLocation = { id: 123 };
        const staticLocation2: IStaticLocation = { id: 456 };
        expectedResult = service.addStaticLocationToCollectionIfMissing([], staticLocation, staticLocation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(staticLocation);
        expect(expectedResult).toContain(staticLocation2);
      });

      it('should accept null and undefined values', () => {
        const staticLocation: IStaticLocation = { id: 123 };
        expectedResult = service.addStaticLocationToCollectionIfMissing([], null, staticLocation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(staticLocation);
      });

      it('should return initial array if no StaticLocation is added', () => {
        const staticLocationCollection: IStaticLocation[] = [{ id: 123 }];
        expectedResult = service.addStaticLocationToCollectionIfMissing(staticLocationCollection, undefined, null);
        expect(expectedResult).toEqual(staticLocationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
