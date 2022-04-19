import dayjs from 'dayjs/esm';

export interface IConfigSetting {
  id?: number;
  sourceType?: string;
  mcUserId?: string | null;
  tmlUserId?: string | null;
  vmSysDefaultModeConf?: string;
  vmSysSyncCycleConf?: number;
  vmSysSyncCycleUnitConf?: string;
  vmSysTargetDisplayNameConf?: string;
  vmLiveDefaultModeConf?: string;
  vmLiveDefaultTimerangeConf?: string;
  vmLivePositionCycleConf?: number;
  vmLivePositionCycleUnitConf?: string;
  vmLiveTrackingAmplitudeConf?: number;
  vmLiveTrackingAmplitudeUnitConf?: string;
  sarSysSyncCycleConf?: number;
  sarSysSyncCycleUnitConf?: string;
  sarSysObjectDisplayName01Conf?: string;
  sarSysObjectDisplayName02Conf?: string;
  createDate?: dayjs.Dayjs;
  createUid?: string;
  lastUpdate?: dayjs.Dayjs;
  lastUpdateUid?: string;
}

export class ConfigSetting implements IConfigSetting {
  constructor(
    public id?: number,
    public sourceType?: string,
    public mcUserId?: string | null,
    public tmlUserId?: string | null,
    public vmSysDefaultModeConf?: string,
    public vmSysSyncCycleConf?: number,
    public vmSysSyncCycleUnitConf?: string,
    public vmSysTargetDisplayNameConf?: string,
    public vmLiveDefaultModeConf?: string,
    public vmLiveDefaultTimerangeConf?: string,
    public vmLivePositionCycleConf?: number,
    public vmLivePositionCycleUnitConf?: string,
    public vmLiveTrackingAmplitudeConf?: number,
    public vmLiveTrackingAmplitudeUnitConf?: string,
    public sarSysSyncCycleConf?: number,
    public sarSysSyncCycleUnitConf?: string,
    public sarSysObjectDisplayName01Conf?: string,
    public sarSysObjectDisplayName02Conf?: string,
    public createDate?: dayjs.Dayjs,
    public createUid?: string,
    public lastUpdate?: dayjs.Dayjs,
    public lastUpdateUid?: string
  ) {}
}

export function getConfigSettingIdentifier(configSetting: IConfigSetting): number | undefined {
  return configSetting.id;
}
