import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITrackingList } from '../tracking-list.model';

@Component({
  selector: 'jhi-tracking-list-detail',
  templateUrl: './tracking-list-detail.component.html',
})
export class TrackingListDetailComponent implements OnInit {
  trackingList: ITrackingList | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trackingList }) => {
      this.trackingList = trackingList;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
