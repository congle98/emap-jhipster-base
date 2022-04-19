import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStaticLocation } from '../static-location.model';

@Component({
  selector: 'jhi-static-location-detail',
  templateUrl: './static-location-detail.component.html',
})
export class StaticLocationDetailComponent implements OnInit {
  staticLocation: IStaticLocation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ staticLocation }) => {
      this.staticLocation = staticLocation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
