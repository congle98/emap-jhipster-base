import dayjs from 'dayjs/esm';

export interface IStaticLocation {
  id?: number;
  name?: string;
  mcUserId?: string;
  address?: string;
  lat?: string;
  lng?: string;
  status?: boolean;
  createDate?: dayjs.Dayjs;
  createUid?: string;
  lastUpdate?: dayjs.Dayjs;
  lastUpdateUid?: string;
}

export class StaticLocation implements IStaticLocation {
  constructor(
    public id?: number,
    public name?: string,
    public mcUserId?: string,
    public address?: string,
    public lat?: string,
    public lng?: string,
    public status?: boolean,
    public createDate?: dayjs.Dayjs,
    public createUid?: string,
    public lastUpdate?: dayjs.Dayjs,
    public lastUpdateUid?: string
  ) {
    this.status = this.status ?? false;
  }
}

export function getStaticLocationIdentifier(staticLocation: IStaticLocation): number | undefined {
  return staticLocation.id;
}
