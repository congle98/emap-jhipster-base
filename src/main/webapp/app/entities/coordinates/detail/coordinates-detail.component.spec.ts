import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CoordinatesDetailComponent } from './coordinates-detail.component';

describe('Coordinates Management Detail Component', () => {
  let comp: CoordinatesDetailComponent;
  let fixture: ComponentFixture<CoordinatesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CoordinatesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ coordinates: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CoordinatesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CoordinatesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load coordinates on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.coordinates).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
