import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { StaticLocationService } from '../service/static-location.service';
import { IStaticLocation, StaticLocation } from '../static-location.model';

import { StaticLocationUpdateComponent } from './static-location-update.component';

describe('StaticLocation Management Update Component', () => {
  let comp: StaticLocationUpdateComponent;
  let fixture: ComponentFixture<StaticLocationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let staticLocationService: StaticLocationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [StaticLocationUpdateComponent],
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
      .overrideTemplate(StaticLocationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StaticLocationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    staticLocationService = TestBed.inject(StaticLocationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const staticLocation: IStaticLocation = { id: 456 };

      activatedRoute.data = of({ staticLocation });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(staticLocation));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<StaticLocation>>();
      const staticLocation = { id: 123 };
      jest.spyOn(staticLocationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ staticLocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: staticLocation }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(staticLocationService.update).toHaveBeenCalledWith(staticLocation);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<StaticLocation>>();
      const staticLocation = new StaticLocation();
      jest.spyOn(staticLocationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ staticLocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: staticLocation }));
      saveSubject.complete();

      // THEN
      expect(staticLocationService.create).toHaveBeenCalledWith(staticLocation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<StaticLocation>>();
      const staticLocation = { id: 123 };
      jest.spyOn(staticLocationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ staticLocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(staticLocationService.update).toHaveBeenCalledWith(staticLocation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
