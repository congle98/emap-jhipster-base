import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WarningRuleDetailComponent } from './warning-rule-detail.component';

describe('WarningRule Management Detail Component', () => {
  let comp: WarningRuleDetailComponent;
  let fixture: ComponentFixture<WarningRuleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WarningRuleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ warningRule: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WarningRuleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WarningRuleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load warningRule on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.warningRule).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
