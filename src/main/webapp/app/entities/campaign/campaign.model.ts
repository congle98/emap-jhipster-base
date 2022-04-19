import dayjs from 'dayjs/esm';

export interface ICampaign {
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

export class Campaign implements ICampaign {
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

export function getCampaignIdentifier(campaign: ICampaign): number | undefined {
  return campaign.id;
}
