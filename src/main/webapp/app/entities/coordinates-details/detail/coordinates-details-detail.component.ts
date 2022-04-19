import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICoordinatesDetails } from '../coordinates-details.model';

@Component({
  selector: 'jhi-coordinates-details-detail',
  templateUrl: './coordinates-details-detail.component.html',
})
export class CoordinatesDetailsDetailComponent implements OnInit {
  coordinatesDetails: ICoordinatesDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ coordinatesDetails }) => {
      this.coordinatesDetails = coordinatesDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
