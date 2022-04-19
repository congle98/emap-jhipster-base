import dayjs from 'dayjs/esm';

export interface ICoordinates {
  id?: number;
  sourceType?: string;
  mcCampaingnId?: string | null;
  tmlCampaignId?: string | null;
  lat?: string | null;
  lng?: string | null;
  radius?: number | null;
  openAngle?: number | null;
  directionalAngle?: number | null;
  createDate?: dayjs.Dayjs;
  createUid?: string;
  lastUpdate?: dayjs.Dayjs;
  lastUpdateUid?: string;
}

export class Coordinates implements ICoordinates {
  constructor(
    public id?: number,
    public sourceType?: string,
    public mcCampaingnId?: string | null,
    public tmlCampaignId?: string | null,
    public lat?: string | null,
    public lng?: string | null,
    public radius?: number | null,
    public openAngle?: number | null,
    public directionalAngle?: number | null,
    public createDate?: dayjs.Dayjs,
    public createUid?: string,
    public lastUpdate?: dayjs.Dayjs,
    public lastUpdateUid?: string
  ) {}
}

export function getCoordinatesIdentifier(coordinates: ICoordinates): number | undefined {
  return coordinates.id;
}
