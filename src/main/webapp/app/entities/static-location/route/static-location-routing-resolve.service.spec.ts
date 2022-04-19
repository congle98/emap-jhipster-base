import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IStaticLocation, StaticLocation } from '../static-location.model';
import { StaticLocationService } from '../service/static-location.service';

import { StaticLocationRoutingResolveService } from './static-location-routing-resolve.service';

describe('StaticLocation routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: StaticLocationRoutingResolveService;
  let service: StaticLocationService;
  let resultStaticLocation: IStaticLocation | undefined;

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
    routingResolveService = TestBed.inject(StaticLocationRoutingResolveService);
    service = TestBed.inject(StaticLocationService);
    resultStaticLocation = undefined;
  });

  describe('resolve', () => {
    it('should return IStaticLocation returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultStaticLocation = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultStaticLocation).toEqual({ id: 123 });
    });

    it('should return new IStaticLocation if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultStaticLocation = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultStaticLocation).toEqual(new StaticLocation());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as StaticLocation })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultStaticLocation = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultStaticLocation).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
