import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WarningMessageService } from '../service/warning-message.service';
import { IWarningMessage, WarningMessage } from '../warning-message.model';
import { IWarningRule } from 'app/entities/warning-rule/warning-rule.model';
import { WarningRuleService } from 'app/entities/warning-rule/service/warning-rule.service';

import { WarningMessageUpdateComponent } from './warning-message-update.component';

describe('WarningMessage Management Update Component', () => {
  let comp: WarningMessageUpdateComponent;
  let fixture: ComponentFixture<WarningMessageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let warningMessageService: WarningMessageService;
  let warningRuleService: WarningRuleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [WarningMessageUpdateComponent],
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
      .overrideTemplate(WarningMessageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WarningMessageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    warningMessageService = TestBed.inject(WarningMessageService);
    warningRuleService = TestBed.inject(WarningRuleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call warningRule query and add missing value', () => {
      const warningMessage: IWarningMessage = { id: 456 };
      const warningRule: IWarningRule = { id: 70778 };
      warningMessage.warningRule = warningRule;

      const warningRuleCollection: IWarningRule[] = [{ id: 92821 }];
      jest.spyOn(warningRuleService, 'query').mockReturnValue(of(new HttpResponse({ body: warningRuleCollection })));
      const expectedCollection: IWarningRule[] = [warningRule, ...warningRuleCollection];
      jest.spyOn(warningRuleService, 'addWarningRuleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ warningMessage });
      comp.ngOnInit();

      expect(warningRuleService.query).toHaveBeenCalled();
      expect(warningRuleService.addWarningRuleToCollectionIfMissing).toHaveBeenCalledWith(warningRuleCollection, warningRule);
      expect(comp.warningRulesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const warningMessage: IWarningMessage = { id: 456 };
      const warningRule: IWarningRule = { id: 19548 };
      warningMessage.warningRule = warningRule;

      activatedRoute.data = of({ warningMessage });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(warningMessage));
      expect(comp.warningRulesCollection).toContain(warningRule);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WarningMessage>>();
      const warningMessage = { id: 123 };
      jest.spyOn(warningMessageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ warningMessage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: warningMessage }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(warningMessageService.update).toHaveBeenCalledWith(warningMessage);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WarningMessage>>();
      const warningMessage = new WarningMessage();
      jest.spyOn(warningMessageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ warningMessage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: warningMessage }));
      saveSubject.complete();

      // THEN
      expect(warningMessageService.create).toHaveBeenCalledWith(warningMessage);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WarningMessage>>();
      const warningMessage = { id: 123 };
      jest.spyOn(warningMessageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ warningMessage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(warningMessageService.update).toHaveBeenCalledWith(warningMessage);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackWarningRuleById', () => {
      it('Should return tracked WarningRule primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackWarningRuleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
