import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CoordinatesService } from '../service/coordinates.service';
import { ICoordinates, Coordinates } from '../coordinates.model';

import { CoordinatesUpdateComponent } from './coordinates-update.component';

describe('Coordinates Management Update Component', () => {
  let comp: CoordinatesUpdateComponent;
  let fixture: ComponentFixture<CoordinatesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let coordinatesService: CoordinatesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CoordinatesUpdateComponent],
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
      .overrideTemplate(CoordinatesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CoordinatesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    coordinatesService = TestBed.inject(CoordinatesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const coordinates: ICoordinates = { id: 456 };

      activatedRoute.data = of({ coordinates });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(coordinates));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Coordinates>>();
      const coordinates = { id: 123 };
      jest.spyOn(coordinatesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ coordinates });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: coordinates }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(coordinatesService.update).toHaveBeenCalledWith(coordinates);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Coordinates>>();
      const coordinates = new Coordinates();
      jest.spyOn(coordinatesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ coordinates });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: coordinates }));
      saveSubject.complete();

      // THEN
      expect(coordinatesService.create).toHaveBeenCalledWith(coordinates);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Coordinates>>();
      const coordinates = { id: 123 };
      jest.spyOn(coordinatesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ coordinates });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(coordinatesService.update).toHaveBeenCalledWith(coordinates);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
