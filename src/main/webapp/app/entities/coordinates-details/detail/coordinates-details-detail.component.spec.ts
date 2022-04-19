import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CoordinatesDetailsDetailComponent } from './coordinates-details-detail.component';

describe('CoordinatesDetails Management Detail Component', () => {
  let comp: CoordinatesDetailsDetailComponent;
  let fixture: ComponentFixture<CoordinatesDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CoordinatesDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ coordinatesDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CoordinatesDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CoordinatesDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load coordinatesDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.coordinatesDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
