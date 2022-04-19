import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICoordinates } from '../coordinates.model';

@Component({
  selector: 'jhi-coordinates-detail',
  templateUrl: './coordinates-detail.component.html',
})
export class CoordinatesDetailComponent implements OnInit {
  coordinates: ICoordinates | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ coordinates }) => {
      this.coordinates = coordinates;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
