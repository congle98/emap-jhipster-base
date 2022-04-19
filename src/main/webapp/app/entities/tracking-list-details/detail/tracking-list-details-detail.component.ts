import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITrackingListDetails } from '../tracking-list-details.model';

@Component({
  selector: 'jhi-tracking-list-details-detail',
  templateUrl: './tracking-list-details-detail.component.html',
})
export class TrackingListDetailsDetailComponent implements OnInit {
  trackingListDetails: ITrackingListDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trackingListDetails }) => {
      this.trackingListDetails = trackingListDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
