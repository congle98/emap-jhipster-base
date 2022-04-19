import dayjs from 'dayjs/esm';
import { ITrackingList } from 'app/entities/tracking-list/tracking-list.model';
import { ITarget } from 'app/entities/target/target.model';

export interface ITrackingListDetails {
  id?: number;
  createDate?: dayjs.Dayjs;
  createUid?: string;
  lastUpdate?: dayjs.Dayjs;
  lastUpdateUid?: string;
  trackingList?: ITrackingList;
  mcTarget?: ITarget;
}

export class TrackingListDetails implements ITrackingListDetails {
  constructor(
    public id?: number,
    public createDate?: dayjs.Dayjs,
    public createUid?: string,
    public lastUpdate?: dayjs.Dayjs,
    public lastUpdateUid?: string,
    public trackingList?: ITrackingList,
    public mcTarget?: ITarget
  ) {}
}

export function getTrackingListDetailsIdentifier(trackingListDetails: ITrackingListDetails): number | undefined {
  return trackingListDetails.id;
}
