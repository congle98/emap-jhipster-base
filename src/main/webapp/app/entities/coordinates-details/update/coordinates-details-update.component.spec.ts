import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CoordinatesDetailsService } from '../service/coordinates-details.service';
import { ICoordinatesDetails, CoordinatesDetails } from '../coordinates-details.model';
import { ICoordinates } from 'app/entities/coordinates/coordinates.model';
import { CoordinatesService } from 'app/entities/coordinates/service/coordinates.service';
import { ITarget } from 'app/entities/target/target.model';
import { TargetService } from 'app/entities/target/service/target.service';

import { CoordinatesDetailsUpdateComponent } from './coordinates-details-update.component';

describe('CoordinatesDetails Management Update Component', () => {
  let comp: CoordinatesDetailsUpdateComponent;
  let fixture: ComponentFixture<CoordinatesDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let coordinatesDetailsService: CoordinatesDetailsService;
  let coordinatesService: CoordinatesService;
  let targetService: TargetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CoordinatesDetailsUpdateComponent],
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
      .overrideTemplate(CoordinatesDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CoordinatesDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    coordinatesDetailsService = TestBed.inject(CoordinatesDetailsService);
    coordinatesService = TestBed.inject(CoordinatesService);
    targetService = TestBed.inject(TargetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call coordinate query and add missing value', () => {
      const coordinatesDetails: ICoordinatesDetails = { id: 456 };
      const coordinate: ICoordinates = { id: 71729 };
      coordinatesDetails.coordinate = coordinate;

      const coordinateCollection: ICoordinates[] = [{ id: 27323 }];
      jest.spyOn(coordinatesService, 'query').mockReturnValue(of(new HttpResponse({ body: coordinateCollection })));
      const expectedCollection: ICoordinates[] = [coordinate, ...coordinateCollection];
      jest.spyOn(coordinatesService, 'addCoordinatesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ coordinatesDetails });
      comp.ngOnInit();

      expect(coordinatesService.query).toHaveBeenCalled();
      expect(coordinatesService.addCoordinatesToCollectionIfMissing).toHaveBeenCalledWith(coordinateCollection, coordinate);
      expect(comp.coordinatesCollection).toEqual(expectedCollection);
    });

    it('Should call object query and add missing value', () => {
      const coordinatesDetails: ICoordinatesDetails = { id: 456 };
      const object: ITarget = { id: 38302 };
      coordinatesDetails.object = object;

      const objectCollection: ITarget[] = [{ id: 73380 }];
      jest.spyOn(targetService, 'query').mockReturnValue(of(new HttpResponse({ body: objectCollection })));
      const expectedCollection: ITarget[] = [object, ...objectCollection];
      jest.spyOn(targetService, 'addTargetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ coordinatesDetails });
      comp.ngOnInit();

      expect(targetService.query).toHaveBeenCalled();
      expect(targetService.addTargetToCollectionIfMissing).toHaveBeenCalledWith(objectCollection, object);
      expect(comp.objectsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const coordinatesDetails: ICoordinatesDetails = { id: 456 };
      const coordinate: ICoordinates = { id: 35713 };
      coordinatesDetails.coordinate = coordinate;
      const object: ITarget = { id: 6076 };
      coordinatesDetails.object = object;

      activatedRoute.data = of({ coordinatesDetails });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(coordinatesDetails));
      expect(comp.coordinatesCollection).toContain(coordinate);
      expect(comp.objectsCollection).toContain(object);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CoordinatesDetails>>();
      const coordinatesDetails = { id: 123 };
      jest.spyOn(coordinatesDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ coordinatesDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: coordinatesDetails }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(coordinatesDetailsService.update).toHaveBeenCalledWith(coordinatesDetails);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CoordinatesDetails>>();
      const coordinatesDetails = new CoordinatesDetails();
      jest.spyOn(coordinatesDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ coordinatesDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: coordinatesDetails }));
      saveSubject.complete();

      // THEN
      expect(coordinatesDetailsService.create).toHaveBeenCalledWith(coordinatesDetails);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CoordinatesDetails>>();
      const coordinatesDetails = { id: 123 };
      jest.spyOn(coordinatesDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ coordinatesDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(coordinatesDetailsService.update).toHaveBeenCalledWith(coordinatesDetails);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCoordinatesById', () => {
      it('Should return tracked Coordinates primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCoordinatesById(0, entity);
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
