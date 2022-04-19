import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICoordinatesDetails, CoordinatesDetails } from '../coordinates-details.model';

import { CoordinatesDetailsService } from './coordinates-details.service';

describe('CoordinatesDetails Service', () => {
  let service: CoordinatesDetailsService;
  let httpMock: HttpTestingController;
  let elemDefault: ICoordinatesDetails;
  let expectedResult: ICoordinatesDetails | ICoordinatesDetails[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CoordinatesDetailsService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      signalConnectionStrength: 0,
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

    it('should create a CoordinatesDetails', () => {
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

      service.create(new CoordinatesDetails()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CoordinatesDetails', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          signalConnectionStrength: 1,
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

    it('should partial update a CoordinatesDetails', () => {
      const patchObject = Object.assign(
        {
          signalConnectionStrength: 1,
          lastUpdate: currentDate.format(DATE_TIME_FORMAT),
        },
        new CoordinatesDetails()
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

    it('should return a list of CoordinatesDetails', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          signalConnectionStrength: 1,
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

    it('should delete a CoordinatesDetails', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCoordinatesDetailsToCollectionIfMissing', () => {
      it('should add a CoordinatesDetails to an empty array', () => {
        const coordinatesDetails: ICoordinatesDetails = { id: 123 };
        expectedResult = service.addCoordinatesDetailsToCollectionIfMissing([], coordinatesDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(coordinatesDetails);
      });

      it('should not add a CoordinatesDetails to an array that contains it', () => {
        const coordinatesDetails: ICoordinatesDetails = { id: 123 };
        const coordinatesDetailsCollection: ICoordinatesDetails[] = [
          {
            ...coordinatesDetails,
          },
          { id: 456 },
        ];
        expectedResult = service.addCoordinatesDetailsToCollectionIfMissing(coordinatesDetailsCollection, coordinatesDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CoordinatesDetails to an array that doesn't contain it", () => {
        const coordinatesDetails: ICoordinatesDetails = { id: 123 };
        const coordinatesDetailsCollection: ICoordinatesDetails[] = [{ id: 456 }];
        expectedResult = service.addCoordinatesDetailsToCollectionIfMissing(coordinatesDetailsCollection, coordinatesDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(coordinatesDetails);
      });

      it('should add only unique CoordinatesDetails to an array', () => {
        const coordinatesDetailsArray: ICoordinatesDetails[] = [{ id: 123 }, { id: 456 }, { id: 17823 }];
        const coordinatesDetailsCollection: ICoordinatesDetails[] = [{ id: 123 }];
        expectedResult = service.addCoordinatesDetailsToCollectionIfMissing(coordinatesDetailsCollection, ...coordinatesDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const coordinatesDetails: ICoordinatesDetails = { id: 123 };
        const coordinatesDetails2: ICoordinatesDetails = { id: 456 };
        expectedResult = service.addCoordinatesDetailsToCollectionIfMissing([], coordinatesDetails, coordinatesDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(coordinatesDetails);
        expect(expectedResult).toContain(coordinatesDetails2);
      });

      it('should accept null and undefined values', () => {
        const coordinatesDetails: ICoordinatesDetails = { id: 123 };
        expectedResult = service.addCoordinatesDetailsToCollectionIfMissing([], null, coordinatesDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(coordinatesDetails);
      });

      it('should return initial array if no CoordinatesDetails is added', () => {
        const coordinatesDetailsCollection: ICoordinatesDetails[] = [{ id: 123 }];
        expectedResult = service.addCoordinatesDetailsToCollectionIfMissing(coordinatesDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(coordinatesDetailsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
