import dayjs from 'dayjs/esm';

export interface IWarningRule {
  id?: number;
  name?: string;
  mcUserId?: string;
  delayCheck?: number;
  delayCheckUnit?: string;
  conditionType?: string;
  includeMcCampaignId?: string | null;
  includeMcTargetId?: string | null;
  warningDistance?: number;
  showWarningCircle?: boolean;
  showWarningMessage?: boolean;
  warningMessage?: string | null;
  sendWarningMessageToMc?: boolean;
  status?: boolean;
  createDate?: dayjs.Dayjs;
  createUid?: string;
  lastUpdate?: dayjs.Dayjs;
  lastUpdateUid?: string;
}

export class WarningRule implements IWarningRule {
  constructor(
    public id?: number,
    public name?: string,
    public mcUserId?: string,
    public delayCheck?: number,
    public delayCheckUnit?: string,
    public conditionType?: string,
    public includeMcCampaignId?: string | null,
    public includeMcTargetId?: string | null,
    public warningDistance?: number,
    public showWarningCircle?: boolean,
    public showWarningMessage?: boolean,
    public warningMessage?: string | null,
    public sendWarningMessageToMc?: boolean,
    public status?: boolean,
    public createDate?: dayjs.Dayjs,
    public createUid?: string,
    public lastUpdate?: dayjs.Dayjs,
    public lastUpdateUid?: string
  ) {
    this.showWarningCircle = this.showWarningCircle ?? false;
    this.showWarningMessage = this.showWarningMessage ?? false;
    this.sendWarningMessageToMc = this.sendWarningMessageToMc ?? false;
    this.status = this.status ?? false;
  }
}

export function getWarningRuleIdentifier(warningRule: IWarningRule): number | undefined {
  return warningRule.id;
}
