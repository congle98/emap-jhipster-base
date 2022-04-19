import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WarningMessageDetailComponent } from './warning-message-detail.component';

describe('WarningMessage Management Detail Component', () => {
  let comp: WarningMessageDetailComponent;
  let fixture: ComponentFixture<WarningMessageDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WarningMessageDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ warningMessage: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WarningMessageDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WarningMessageDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load warningMessage on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.warningMessage).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
