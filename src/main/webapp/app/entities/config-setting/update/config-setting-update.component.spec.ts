import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ConfigSettingService } from '../service/config-setting.service';
import { IConfigSetting, ConfigSetting } from '../config-setting.model';

import { ConfigSettingUpdateComponent } from './config-setting-update.component';

describe('ConfigSetting Management Update Component', () => {
  let comp: ConfigSettingUpdateComponent;
  let fixture: ComponentFixture<ConfigSettingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let configSettingService: ConfigSettingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ConfigSettingUpdateComponent],
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
      .overrideTemplate(ConfigSettingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConfigSettingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    configSettingService = TestBed.inject(ConfigSettingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const configSetting: IConfigSetting = { id: 456 };

      activatedRoute.data = of({ configSetting });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(configSetting));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ConfigSetting>>();
      const configSetting = { id: 123 };
      jest.spyOn(configSettingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ configSetting });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: configSetting }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(configSettingService.update).toHaveBeenCalledWith(configSetting);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ConfigSetting>>();
      const configSetting = new ConfigSetting();
      jest.spyOn(configSettingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ configSetting });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: configSetting }));
      saveSubject.complete();

      // THEN
      expect(configSettingService.create).toHaveBeenCalledWith(configSetting);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ConfigSetting>>();
      const configSetting = { id: 123 };
      jest.spyOn(configSettingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ configSetting });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(configSettingService.update).toHaveBeenCalledWith(configSetting);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
