import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICoordinates, Coordinates } from '../coordinates.model';

import { CoordinatesService } from './coordinates.service';

describe('Coordinates Service', () => {
  let service: CoordinatesService;
  let httpMock: HttpTestingController;
  let elemDefault: ICoordinates;
  let expectedResult: ICoordinates | ICoordinates[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CoordinatesService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      sourceType: 'AAAAAAA',
      mcCampaingnId: 'AAAAAAA',
      tmlCampaignId: 'AAAAAAA',
      lat: 'AAAAAAA',
      lng: 'AAAAAAA',
      radius: 0,
      openAngle: 0,
      directionalAngle: 0,
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

    it('should create a Coordinates', () => {
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

      service.create(new Coordinates()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Coordinates', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          sourceType: 'BBBBBB',
          mcCampaingnId: 'BBBBBB',
          tmlCampaignId: 'BBBBBB',
          lat: 'BBBBBB',
          lng: 'BBBBBB',
          radius: 1,
          openAngle: 1,
          directionalAngle: 1,
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

    it('should partial update a Coordinates', () => {
      const patchObject = Object.assign(
        {
          sourceType: 'BBBBBB',
          mcCampaingnId: 'BBBBBB',
          lng: 'BBBBBB',
          radius: 1,
          openAngle: 1,
          lastUpdate: currentDate.format(DATE_TIME_FORMAT),
        },
        new Coordinates()
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

    it('should return a list of Coordinates', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          sourceType: 'BBBBBB',
          mcCampaingnId: 'BBBBBB',
          tmlCampaignId: 'BBBBBB',
          lat: 'BBBBBB',
          lng: 'BBBBBB',
          radius: 1,
          openAngle: 1,
          directionalAngle: 1,
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

    it('should delete a Coordinates', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCoordinatesToCollectionIfMissing', () => {
      it('should add a Coordinates to an empty array', () => {
        const coordinates: ICoordinates = { id: 123 };
        expectedResult = service.addCoordinatesToCollectionIfMissing([], coordinates);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(coordinates);
      });

      it('should not add a Coordinates to an array that contains it', () => {
        const coordinates: ICoordinates = { id: 123 };
        const coordinatesCollection: ICoordinates[] = [
          {
            ...coordinates,
          },
          { id: 456 },
        ];
        expectedResult = service.addCoordinatesToCollectionIfMissing(coordinatesCollection, coordinates);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Coordinates to an array that doesn't contain it", () => {
        const coordinates: ICoordinates = { id: 123 };
        const coordinatesCollection: ICoordinates[] = [{ id: 456 }];
        expectedResult = service.addCoordinatesToCollectionIfMissing(coordinatesCollection, coordinates);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(coordinates);
      });

      it('should add only unique Coordinates to an array', () => {
        const coordinatesArray: ICoordinates[] = [{ id: 123 }, { id: 456 }, { id: 82461 }];
        const coordinatesCollection: ICoordinates[] = [{ id: 123 }];
        expectedResult = service.addCoordinatesToCollectionIfMissing(coordinatesCollection, ...coordinatesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const coordinates: ICoordinates = { id: 123 };
        const coordinates2: ICoordinates = { id: 456 };
        expectedResult = service.addCoordinatesToCollectionIfMissing([], coordinates, coordinates2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(coordinates);
        expect(expectedResult).toContain(coordinates2);
      });

      it('should accept null and undefined values', () => {
        const coordinates: ICoordinates = { id: 123 };
        expectedResult = service.addCoordinatesToCollectionIfMissing([], null, coordinates, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(coordinates);
      });

      it('should return initial array if no Coordinates is added', () => {
        const coordinatesCollection: ICoordinates[] = [{ id: 123 }];
        expectedResult = service.addCoordinatesToCollectionIfMissing(coordinatesCollection, undefined, null);
        expect(expectedResult).toEqual(coordinatesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
