import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TrackingListService } from '../service/tracking-list.service';
import { ITrackingList, TrackingList } from '../tracking-list.model';

import { TrackingListUpdateComponent } from './tracking-list-update.component';

describe('TrackingList Management Update Component', () => {
  let comp: TrackingListUpdateComponent;
  let fixture: ComponentFixture<TrackingListUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trackingListService: TrackingListService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TrackingListUpdateComponent],
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
      .overrideTemplate(TrackingListUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrackingListUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trackingListService = TestBed.inject(TrackingListService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const trackingList: ITrackingList = { id: 456 };

      activatedRoute.data = of({ trackingList });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(trackingList));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TrackingList>>();
      const trackingList = { id: 123 };
      jest.spyOn(trackingListService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trackingList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trackingList }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(trackingListService.update).toHaveBeenCalledWith(trackingList);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TrackingList>>();
      const trackingList = new TrackingList();
      jest.spyOn(trackingListService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trackingList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trackingList }));
      saveSubject.complete();

      // THEN
      expect(trackingListService.create).toHaveBeenCalledWith(trackingList);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TrackingList>>();
      const trackingList = { id: 123 };
      jest.spyOn(trackingListService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trackingList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trackingListService.update).toHaveBeenCalledWith(trackingList);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
