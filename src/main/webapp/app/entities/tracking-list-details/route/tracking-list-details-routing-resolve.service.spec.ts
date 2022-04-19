import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITrackingListDetails, TrackingListDetails } from '../tracking-list-details.model';
import { TrackingListDetailsService } from '../service/tracking-list-details.service';

import { TrackingListDetailsRoutingResolveService } from './tracking-list-details-routing-resolve.service';

describe('TrackingListDetails routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TrackingListDetailsRoutingResolveService;
  let service: TrackingListDetailsService;
  let resultTrackingListDetails: ITrackingListDetails | undefined;

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
    routingResolveService = TestBed.inject(TrackingListDetailsRoutingResolveService);
    service = TestBed.inject(TrackingListDetailsService);
    resultTrackingListDetails = undefined;
  });

  describe('resolve', () => {
    it('should return ITrackingListDetails returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTrackingListDetails = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTrackingListDetails).toEqual({ id: 123 });
    });

    it('should return new ITrackingListDetails if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTrackingListDetails = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTrackingListDetails).toEqual(new TrackingListDetails());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TrackingListDetails })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTrackingListDetails = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTrackingListDetails).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
