import dayjs from 'dayjs/esm';
import { ICoordinates } from 'app/entities/coordinates/coordinates.model';
import { ITarget } from 'app/entities/target/target.model';

export interface ICoordinatesDetails {
  id?: number;
  signalConnectionStrength?: number | null;
  createDate?: dayjs.Dayjs;
  createUid?: string;
  lastUpdate?: dayjs.Dayjs;
  lastUpdateUid?: string;
  coordinate?: ICoordinates;
  object?: ITarget;
}

export class CoordinatesDetails implements ICoordinatesDetails {
  constructor(
    public id?: number,
    public signalConnectionStrength?: number | null,
    public createDate?: dayjs.Dayjs,
    public createUid?: string,
    public lastUpdate?: dayjs.Dayjs,
    public lastUpdateUid?: string,
    public coordinate?: ICoordinates,
    public object?: ITarget
  ) {}
}

export function getCoordinatesDetailsIdentifier(coordinatesDetails: ICoordinatesDetails): number | undefined {
  return coordinatesDetails.id;
}
