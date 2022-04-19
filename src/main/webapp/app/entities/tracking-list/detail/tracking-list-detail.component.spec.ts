import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TrackingListDetailComponent } from './tracking-list-detail.component';

describe('TrackingList Management Detail Component', () => {
  let comp: TrackingListDetailComponent;
  let fixture: ComponentFixture<TrackingListDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TrackingListDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ trackingList: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TrackingListDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TrackingListDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load trackingList on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.trackingList).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
