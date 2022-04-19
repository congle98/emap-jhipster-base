import dayjs from 'dayjs/esm';
import { IWarningRule } from 'app/entities/warning-rule/warning-rule.model';

export interface IWarningMessage {
  id?: number;
  mcUserId?: string;
  warningDistance?: string;
  showWarningCircle?: boolean;
  showWarningMessage?: boolean;
  warningMessage?: string | null;
  sendWarningMessageToMc?: boolean;
  createDate?: dayjs.Dayjs;
  createUid?: string;
  lastUpdate?: dayjs.Dayjs;
  lastUpdateUid?: string;
  warningRule?: IWarningRule;
}

export class WarningMessage implements IWarningMessage {
  constructor(
    public id?: number,
    public mcUserId?: string,
    public warningDistance?: string,
    public showWarningCircle?: boolean,
    public showWarningMessage?: boolean,
    public warningMessage?: string | null,
    public sendWarningMessageToMc?: boolean,
    public createDate?: dayjs.Dayjs,
    public createUid?: string,
    public lastUpdate?: dayjs.Dayjs,
    public lastUpdateUid?: string,
    public warningRule?: IWarningRule
  ) {
    this.showWarningCircle = this.showWarningCircle ?? false;
    this.showWarningMessage = this.showWarningMessage ?? false;
    this.sendWarningMessageToMc = this.sendWarningMessageToMc ?? false;
  }
}

export function getWarningMessageIdentifier(warningMessage: IWarningMessage): number | undefined {
  return warningMessage.id;
}
