import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IWarningMessage, WarningMessage } from '../warning-message.model';
import { WarningMessageService } from '../service/warning-message.service';

import { WarningMessageRoutingResolveService } from './warning-message-routing-resolve.service';

describe('WarningMessage routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: WarningMessageRoutingResolveService;
  let service: WarningMessageService;
  let resultWarningMessage: IWarningMessage | undefined;

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
    routingResolveService = TestBed.inject(WarningMessageRoutingResolveService);
    service = TestBed.inject(WarningMessageService);
    resultWarningMessage = undefined;
  });

  describe('resolve', () => {
    it('should return IWarningMessage returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWarningMessage = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWarningMessage).toEqual({ id: 123 });
    });

    it('should return new IWarningMessage if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWarningMessage = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultWarningMessage).toEqual(new WarningMessage());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as WarningMessage })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWarningMessage = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWarningMessage).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
