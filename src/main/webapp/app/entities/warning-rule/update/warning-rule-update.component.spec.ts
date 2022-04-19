import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WarningRuleService } from '../service/warning-rule.service';
import { IWarningRule, WarningRule } from '../warning-rule.model';

import { WarningRuleUpdateComponent } from './warning-rule-update.component';

describe('WarningRule Management Update Component', () => {
  let comp: WarningRuleUpdateComponent;
  let fixture: ComponentFixture<WarningRuleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let warningRuleService: WarningRuleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [WarningRuleUpdateComponent],
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
      .overrideTemplate(WarningRuleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WarningRuleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    warningRuleService = TestBed.inject(WarningRuleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const warningRule: IWarningRule = { id: 456 };

      activatedRoute.data = of({ warningRule });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(warningRule));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WarningRule>>();
      const warningRule = { id: 123 };
      jest.spyOn(warningRuleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ warningRule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: warningRule }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(warningRuleService.update).toHaveBeenCalledWith(warningRule);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WarningRule>>();
      const warningRule = new WarningRule();
      jest.spyOn(warningRuleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ warningRule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: warningRule }));
      saveSubject.complete();

      // THEN
      expect(warningRuleService.create).toHaveBeenCalledWith(warningRule);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WarningRule>>();
      const warningRule = { id: 123 };
      jest.spyOn(warningRuleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ warningRule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(warningRuleService.update).toHaveBeenCalledWith(warningRule);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
