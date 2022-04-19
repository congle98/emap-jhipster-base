import dayjs from 'dayjs/esm';

export interface ITrackingList {
  id?: number;
  mcUserId?: string;
  type?: string;
  createDate?: dayjs.Dayjs;
  createUid?: string;
  lastUpdate?: dayjs.Dayjs;
  lastUpdateUid?: string;
}

export class TrackingList implements ITrackingList {
  constructor(
    public id?: number,
    public mcUserId?: string,
    public type?: string,
    public createDate?: dayjs.Dayjs,
    public createUid?: string,
    public lastUpdate?: dayjs.Dayjs,
    public lastUpdateUid?: string
  ) {}
}

export function getTrackingListIdentifier(trackingList: ITrackingList): number | undefined {
  return trackingList.id;
}
