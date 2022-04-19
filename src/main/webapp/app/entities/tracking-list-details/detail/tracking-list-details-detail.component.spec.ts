import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TrackingListDetailsDetailComponent } from './tracking-list-details-detail.component';

describe('TrackingListDetails Management Detail Component', () => {
  let comp: TrackingListDetailsDetailComponent;
  let fixture: ComponentFixture<TrackingListDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TrackingListDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ trackingListDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TrackingListDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TrackingListDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load trackingListDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.trackingListDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
