import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWarningMessage } from '../warning-message.model';

@Component({
  selector: 'jhi-warning-message-detail',
  templateUrl: './warning-message-detail.component.html',
})
export class WarningMessageDetailComponent implements OnInit {
  warningMessage: IWarningMessage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ warningMessage }) => {
      this.warningMessage = warningMessage;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
