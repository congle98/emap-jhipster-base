import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TrackingListDetailsService } from '../service/tracking-list-details.service';
import { ITrackingListDetails, TrackingListDetails } from '../tracking-list-details.model';
import { ITrackingList } from 'app/entities/tracking-list/tracking-list.model';
import { TrackingListService } from 'app/entities/tracking-list/service/tracking-list.service';
import { ITarget } from 'app/entities/target/target.model';
import { TargetService } from 'app/entities/target/service/target.service';

import { TrackingListDetailsUpdateComponent } from './tracking-list-details-update.component';

describe('TrackingListDetails Management Update Component', () => {
  let comp: TrackingListDetailsUpdateComponent;
  let fixture: ComponentFixture<TrackingListDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trackingListDetailsService: TrackingListDetailsService;
  let trackingListService: TrackingListService;
  let targetService: TargetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TrackingListDetailsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TrackingListDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrackingListDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trackingListDetailsService = TestBed.inject(TrackingListDetailsService);
    trackingListService = TestBed.inject(TrackingListService);
    targetService = TestBed.inject(TargetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call trackingList query and add missing value', () => {
      const trackingListDetails: ITrackingListDetails = { id: 456 };
      const trackingList: ITrackingList = { id: 65782 };
      trackingListDetails.trackingList = trackingList;

      const trackingListCollection: ITrackingList[] = [{ id: 37199 }];
      jest.spyOn(trackingListService, 'query').mockReturnValue(of(new HttpResponse({ body: trackingListCollection })));
      const expectedCollection: ITrackingList[] = [trackingList, ...trackingListCollection];
      jest.spyOn(trackingListService, 'addTrackingListToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trackingListDetails });
      comp.ngOnInit();

      expect(trackingListService.query).toHaveBeenCalled();
      expect(trackingListService.addTrackingListToCollectionIfMissing).toHaveBeenCalledWith(trackingListCollection, trackingList);
      expect(comp.trackingListsCollection).toEqual(expectedCollection);
    });

    it('Should call mcTarget query and add missing value', () => {
      const trackingListDetails: ITrackingListDetails = { id: 456 };
      const mcTarget: ITarget = { id: 76954 };
      trackingListDetails.mcTarget = mcTarget;

      const mcTargetCollection: ITarget[] = [{ id: 48814 }];
      jest.spyOn(targetService, 'query').mockReturnValue(of(new HttpResponse({ body: mcTargetCollection })));
      const expectedCollection: ITarget[] = [mcTarget, ...mcTargetCollection];
      jest.spyOn(targetService, 'addTargetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trackingListDetails });
      comp.ngOnInit();

      expect(targetService.query).toHaveBeenCalled();
      expect(targetService.addTargetToCollectionIfMissing).toHaveBeenCalledWith(mcTargetCollection, mcTarget);
      expect(comp.mcTargetsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const trackingListDetails: ITrackingListDetails = { id: 456 };
      const trackingList: ITrackingList = { id: 8155 };
      trackingListDetails.trackingList = trackingList;
      const mcTarget: ITarget = { id: 9112 };
      trackingListDetails.mcTarget = mcTarget;

      activatedRoute.data = of({ trackingListDetails });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(trackingListDetails));
      expect(comp.trackingListsCollection).toContain(trackingList);
      expect(comp.mcTargetsCollection).toContain(mcTarget);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TrackingListDetails>>();
      const trackingListDetails = { id: 123 };
      jest.spyOn(trackingListDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trackingListDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trackingListDetails }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(trackingListDetailsService.update).toHaveBeenCalledWith(trackingListDetails);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TrackingListDetails>>();
      const trackingListDetails = new TrackingListDetails();
      jest.spyOn(trackingListDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trackingListDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trackingListDetails }));
      saveSubject.complete();

      // THEN
      expect(trackingListDetailsService.create).toHaveBeenCalledWith(trackingListDetails);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TrackingListDetails>>();
      const trackingListDetails = { id: 123 };
      jest.spyOn(trackingListDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trackingListDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trackingListDetailsService.update).toHaveBeenCalledWith(trackingListDetails);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTrackingListById', () => {
      it('Should return tracked TrackingList primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTrackingListById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTargetById', () => {
      it('Should return tracked Target primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTargetById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
