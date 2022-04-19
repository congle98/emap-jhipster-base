import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TargetService } from '../service/target.service';
import { ITarget, Target } from '../target.model';

import { TargetUpdateComponent } from './target-update.component';

describe('Target Management Update Component', () => {
  let comp: TargetUpdateComponent;
  let fixture: ComponentFixture<TargetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let targetService: TargetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TargetUpdateComponent],
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
      .overrideTemplate(TargetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TargetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    targetService = TestBed.inject(TargetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const target: ITarget = { id: 456 };

      activatedRoute.data = of({ target });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(target));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Target>>();
      const target = { id: 123 };
      jest.spyOn(targetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ target });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: target }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(targetService.update).toHaveBeenCalledWith(target);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Target>>();
      const target = new Target();
      jest.spyOn(targetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ target });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: target }));
      saveSubject.complete();

      // THEN
      expect(targetService.create).toHaveBeenCalledWith(target);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Target>>();
      const target = { id: 123 };
      jest.spyOn(targetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ target });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(targetService.update).toHaveBeenCalledWith(target);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
