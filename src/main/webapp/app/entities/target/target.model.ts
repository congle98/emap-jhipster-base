import dayjs from 'dayjs/esm';

export interface ITarget {
  id?: number;
  sourceType?: string;
  mcCampaingnId?: string | null;
  tmlCampaignId?: string | null;
  icon?: string | null;
  color?: string | null;
  createDate?: dayjs.Dayjs;
  createUid?: string;
  lastUpdate?: dayjs.Dayjs;
  lastUpdateUid?: string;
}

export class Target implements ITarget {
  constructor(
    public id?: number,
    public sourceType?: string,
    public mcCampaingnId?: string | null,
    public tmlCampaignId?: string | null,
    public icon?: string | null,
    public color?: string | null,
    public createDate?: dayjs.Dayjs,
    public createUid?: string,
    public lastUpdate?: dayjs.Dayjs,
    public lastUpdateUid?: string
  ) {}
}

export function getTargetIdentifier(target: ITarget): number | undefined {
  return target.id;
}
