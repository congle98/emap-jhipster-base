import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StaticLocationDetailComponent } from './static-location-detail.component';

describe('StaticLocation Management Detail Component', () => {
  let comp: StaticLocationDetailComponent;
  let fixture: ComponentFixture<StaticLocationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StaticLocationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ staticLocation: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(StaticLocationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(StaticLocationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load staticLocation on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.staticLocation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
