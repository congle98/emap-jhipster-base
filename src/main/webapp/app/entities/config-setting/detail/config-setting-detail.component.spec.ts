import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ConfigSettingDetailComponent } from './config-setting-detail.component';

describe('ConfigSetting Management Detail Component', () => {
  let comp: ConfigSettingDetailComponent;
  let fixture: ComponentFixture<ConfigSettingDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ConfigSettingDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ configSetting: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ConfigSettingDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ConfigSettingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load configSetting on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.configSetting).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
