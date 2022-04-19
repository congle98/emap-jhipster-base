import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWarningRule } from '../warning-rule.model';

@Component({
  selector: 'jhi-warning-rule-detail',
  templateUrl: './warning-rule-detail.component.html',
})
export class WarningRuleDetailComponent implements OnInit {
  warningRule: IWarningRule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ warningRule }) => {
      this.warningRule = warningRule;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
