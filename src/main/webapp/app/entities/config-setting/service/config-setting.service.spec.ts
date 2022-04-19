import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IConfigSetting, ConfigSetting } from '../config-setting.model';

import { ConfigSettingService } from './config-setting.service';

describe('ConfigSetting Service', () => {
  let service: ConfigSettingService;
  let httpMock: HttpTestingController;
  let elemDefault: IConfigSetting;
  let expectedResult: IConfigSetting | IConfigSetting[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ConfigSettingService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      sourceType: 'AAAAAAA',
      mcUserId: 'AAAAAAA',
      tmlUserId: 'AAAAAAA',
      vmSysDefaultModeConf: 'AAAAAAA',
      vmSysSyncCycleConf: 0,
      vmSysSyncCycleUnitConf: 'AAAAAAA',
      vmSysTargetDisplayNameConf: 'AAAAAAA',
      vmLiveDefaultModeConf: 'AAAAAAA',
      vmLiveDefaultTimerangeConf: 'AAAAAAA',
      vmLivePositionCycleConf: 0,
      vmLivePositionCycleUnitConf: 'AAAAAAA',
      vmLiveTrackingAmplitudeConf: 0,
      vmLiveTrackingAmplitudeUnitConf: 'AAAAAAA',
      sarSysSyncCycleConf: 0,
      sarSysSyncCycleUnitConf: 'AAAAAAA',
      sarSysObjectDisplayName01Conf: 'AAAAAAA',
      sarSysObjectDisplayName02Conf: 'AAAAAAA',
      createDate: currentDate,
      createUid: 'AAAAAAA',
      lastUpdate: currentDate,
      lastUpdateUid: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          createDate: currentDate.format(DATE_TIME_FORMAT),
          lastUpdate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ConfigSetting', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          createDate: currentDate.format(DATE_TIME_FORMAT),
          lastUpdate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createDate: currentDate,
          lastUpdate: currentDate,
        },
        returnedFromService
      );

      service.create(new ConfigSetting()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ConfigSetting', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          sourceType: 'BBBBBB',
          mcUserId: 'BBBBBB',
          tmlUserId: 'BBBBBB',
          vmSysDefaultModeConf: 'BBBBBB',
          vmSysSyncCycleConf: 1,
          vmSysSyncCycleUnitConf: 'BBBBBB',
          vmSysTargetDisplayNameConf: 'BBBBBB',
          vmLiveDefaultModeConf: 'BBBBBB',
          vmLiveDefaultTimerangeConf: 'BBBBBB',
          vmLivePositionCycleConf: 1,
          vmLivePositionCycleUnitConf: 'BBBBBB',
          vmLiveTrackingAmplitudeConf: 1,
          vmLiveTrackingAmplitudeUnitConf: 'BBBBBB',
          sarSysSyncCycleConf: 1,
          sarSysSyncCycleUnitConf: 'BBBBBB',
          sarSysObjectDisplayName01Conf: 'BBBBBB',
          sarSysObjectDisplayName02Conf: 'BBBBBB',
          createDate: currentDate.format(DATE_TIME_FORMAT),
          createUid: 'BBBBBB',
          lastUpdate: currentDate.format(DATE_TIME_FORMAT),
          lastUpdateUid: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createDate: currentDate,
          lastUpdate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ConfigSetting', () => {
      const patchObject = Object.assign(
        {
          mcUserId: 'BBBBBB',
          vmSysSyncCycleConf: 1,
          vmLiveDefaultModeConf: 'BBBBBB',
          vmLivePositionCycleConf: 1,
          vmLiveTrackingAmplitudeUnitConf: 'BBBBBB',
          sarSysSyncCycleConf: 1,
          sarSysSyncCycleUnitConf: 'BBBBBB',
          sarSysObjectDisplayName02Conf: 'BBBBBB',
          lastUpdate: currentDate.format(DATE_TIME_FORMAT),
          lastUpdateUid: 'BBBBBB',
        },
        new ConfigSetting()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          createDate: currentDate,
          lastUpdate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ConfigSetting', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          sourceType: 'BBBBBB',
          mcUserId: 'BBBBBB',
          tmlUserId: 'BBBBBB',
          vmSysDefaultModeConf: 'BBBBBB',
          vmSysSyncCycleConf: 1,
          vmSysSyncCycleUnitConf: 'BBBBBB',
          vmSysTargetDisplayNameConf: 'BBBBBB',
          vmLiveDefaultModeConf: 'BBBBBB',
          vmLiveDefaultTimerangeConf: 'BBBBBB',
          vmLivePositionCycleConf: 1,
          vmLivePositionCycleUnitConf: 'BBBBBB',
          vmLiveTrackingAmplitudeConf: 1,
          vmLiveTrackingAmplitudeUnitConf: 'BBBBBB',
          sarSysSyncCycleConf: 1,
          sarSysSyncCycleUnitConf: 'BBBBBB',
          sarSysObjectDisplayName01Conf: 'BBBBBB',
          sarSysObjectDisplayName02Conf: 'BBBBBB',
          createDate: currentDate.format(DATE_TIME_FORMAT),
          createUid: 'BBBBBB',
          lastUpdate: currentDate.format(DATE_TIME_FORMAT),
          lastUpdateUid: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createDate: currentDate,
          lastUpdate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ConfigSetting', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addConfigSettingToCollectionIfMissing', () => {
      it('should add a ConfigSetting to an empty array', () => {
        const configSetting: IConfigSetting = { id: 123 };
        expectedResult = service.addConfigSettingToCollectionIfMissing([], configSetting);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(configSetting);
      });

      it('should not add a ConfigSetting to an array that contains it', () => {
        const configSetting: IConfigSetting = { id: 123 };
        const configSettingCollection: IConfigSetting[] = [
          {
            ...configSetting,
          },
          { id: 456 },
        ];
        expectedResult = service.addConfigSettingToCollectionIfMissing(configSettingCollection, configSetting);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ConfigSetting to an array that doesn't contain it", () => {
        const configSetting: IConfigSetting = { id: 123 };
        const configSettingCollection: IConfigSetting[] = [{ id: 456 }];
        expectedResult = service.addConfigSettingToCollectionIfMissing(configSettingCollection, configSetting);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(configSetting);
      });

      it('should add only unique ConfigSetting to an array', () => {
        const configSettingArray: IConfigSetting[] = [{ id: 123 }, { id: 456 }, { id: 83022 }];
        const configSettingCollection: IConfigSetting[] = [{ id: 123 }];
        expectedResult = service.addConfigSettingToCollectionIfMissing(configSettingCollection, ...configSettingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const configSetting: IConfigSetting = { id: 123 };
        const configSetting2: IConfigSetting = { id: 456 };
        expectedResult = service.addConfigSettingToCollectionIfMissing([], configSetting, configSetting2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(configSetting);
        expect(expectedResult).toContain(configSetting2);
      });

      it('should accept null and undefined values', () => {
        const configSetting: IConfigSetting = { id: 123 };
        expectedResult = service.addConfigSettingToCollectionIfMissing([], null, configSetting, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(configSetting);
      });

      it('should return initial array if no ConfigSetting is added', () => {
        const configSettingCollection: IConfigSetting[] = [{ id: 123 }];
        expectedResult = service.addConfigSettingToCollectionIfMissing(configSettingCollection, undefined, null);
        expect(expectedResult).toEqual(configSettingCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
