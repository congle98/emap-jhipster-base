import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICoordinates, Coordinates } from '../coordinates.model';
import { CoordinatesService } from '../service/coordinates.service';

import { CoordinatesRoutingResolveService } from './coordinates-routing-resolve.service';

describe('Coordinates routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CoordinatesRoutingResolveService;
  let service: CoordinatesService;
  let resultCoordinates: ICoordinates | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(CoordinatesRoutingResolveService);
    service = TestBed.inject(CoordinatesService);
    resultCoordinates = undefined;
  });

  describe('resolve', () => {
    it('should return ICoordinates returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCoordinates = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCoordinates).toEqual({ id: 123 });
    });

    it('should return new ICoordinates if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCoordinates = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCoordinates).toEqual(new Coordinates());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Coordinates })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCoordinates = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCoordinates).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
