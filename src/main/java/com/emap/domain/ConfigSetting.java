package com.emap.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ConfigSetting.
 */
@Entity
@Table(name = "config_setting")
public class ConfigSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "source_type", length = 20, nullable = false)
    private String sourceType;

    @Size(max = 15)
    @Column(name = "mc_user_id", length = 15)
    private String mcUserId;

    @Size(max = 15)
    @Column(name = "tml_user_id", length = 15)
    private String tmlUserId;

    @NotNull
    @Size(max = 20)
    @Column(name = "vm_sys_default_mode_conf", length = 20, nullable = false)
    private String vmSysDefaultModeConf;

    @NotNull
    @Column(name = "vm_sys_sync_cycle_conf", nullable = false)
    private Integer vmSysSyncCycleConf;

    @NotNull
    @Column(name = "vm_sys_sync_cycle_unit_conf", nullable = false)
    private String vmSysSyncCycleUnitConf;

    @NotNull
    @Size(max = 20)
    @Column(name = "vm_sys_target_display_name_conf", length = 20, nullable = false)
    private String vmSysTargetDisplayNameConf;

    @NotNull
    @Column(name = "vm_live_default_mode_conf", nullable = false)
    private String vmLiveDefaultModeConf;

    @NotNull
    @Column(name = "vm_live_default_timerange_conf", nullable = false)
    private String vmLiveDefaultTimerangeConf;

    @NotNull
    @Column(name = "vm_live_position_cycle_conf", nullable = false)
    private Integer vmLivePositionCycleConf;

    @NotNull
    @Column(name = "vm_live_position_cycle_unit_conf", nullable = false)
    private String vmLivePositionCycleUnitConf;

    @NotNull
    @Column(name = "vm_live_tracking_amplitude_conf", nullable = false)
    private Integer vmLiveTrackingAmplitudeConf;

    @NotNull
    @Column(name = "vm_live_tracking_amplitude_unit_conf", nullable = false)
    private String vmLiveTrackingAmplitudeUnitConf;

    @NotNull
    @Column(name = "sar_sys_sync_cycle_conf", nullable = false)
    private Integer sarSysSyncCycleConf;

    @NotNull
    @Column(name = "sar_sys_sync_cycle_unit_conf", nullable = false)
    private String sarSysSyncCycleUnitConf;

    @NotNull
    @Size(max = 255)
    @Column(name = "sar_sys_object_display_name_01_conf", length = 255, nullable = false)
    private String sarSysObjectDisplayName01Conf;

    @NotNull
    @Size(max = 255)
    @Column(name = "sar_sys_object_display_name_02_conf", length = 255, nullable = false)
    private String sarSysObjectDisplayName02Conf;

    @NotNull
    @Column(name = "create_date", nullable = false)
    private Instant createDate;

    @NotNull
    @Size(max = 15)
    @Column(name = "create_uid", length = 15, nullable = false)
    private String createUid;

    @NotNull
    @Column(name = "last_update", nullable = false)
    private Instant lastUpdate;

    @NotNull
    @Size(max = 15)
    @Column(name = "last_update_uid", length = 15, nullable = false)
    private String lastUpdateUid;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ConfigSetting id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceType() {
        return this.sourceType;
    }

    public ConfigSetting sourceType(String sourceType) {
        this.setSourceType(sourceType);
        return this;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getMcUserId() {
        return this.mcUserId;
    }

    public ConfigSetting mcUserId(String mcUserId) {
        this.setMcUserId(mcUserId);
        return this;
    }

    public void setMcUserId(String mcUserId) {
        this.mcUserId = mcUserId;
    }

    public String getTmlUserId() {
        return this.tmlUserId;
    }

    public ConfigSetting tmlUserId(String tmlUserId) {
        this.setTmlUserId(tmlUserId);
        return this;
    }

    public void setTmlUserId(String tmlUserId) {
        this.tmlUserId = tmlUserId;
    }

    public String getVmSysDefaultModeConf() {
        return this.vmSysDefaultModeConf;
    }

    public ConfigSetting vmSysDefaultModeConf(String vmSysDefaultModeConf) {
        this.setVmSysDefaultModeConf(vmSysDefaultModeConf);
        return this;
    }

    public void setVmSysDefaultModeConf(String vmSysDefaultModeConf) {
        this.vmSysDefaultModeConf = vmSysDefaultModeConf;
    }

    public Integer getVmSysSyncCycleConf() {
        return this.vmSysSyncCycleConf;
    }

    public ConfigSetting vmSysSyncCycleConf(Integer vmSysSyncCycleConf) {
        this.setVmSysSyncCycleConf(vmSysSyncCycleConf);
        return this;
    }

    public void setVmSysSyncCycleConf(Integer vmSysSyncCycleConf) {
        this.vmSysSyncCycleConf = vmSysSyncCycleConf;
    }

    public String getVmSysSyncCycleUnitConf() {
        return this.vmSysSyncCycleUnitConf;
    }

    public ConfigSetting vmSysSyncCycleUnitConf(String vmSysSyncCycleUnitConf) {
        this.setVmSysSyncCycleUnitConf(vmSysSyncCycleUnitConf);
        return this;
    }

    public void setVmSysSyncCycleUnitConf(String vmSysSyncCycleUnitConf) {
        this.vmSysSyncCycleUnitConf = vmSysSyncCycleUnitConf;
    }

    public String getVmSysTargetDisplayNameConf() {
        return this.vmSysTargetDisplayNameConf;
    }

    public ConfigSetting vmSysTargetDisplayNameConf(String vmSysTargetDisplayNameConf) {
        this.setVmSysTargetDisplayNameConf(vmSysTargetDisplayNameConf);
        return this;
    }

    public void setVmSysTargetDisplayNameConf(String vmSysTargetDisplayNameConf) {
        this.vmSysTargetDisplayNameConf = vmSysTargetDisplayNameConf;
    }

    public String getVmLiveDefaultModeConf() {
        return this.vmLiveDefaultModeConf;
    }

    public ConfigSetting vmLiveDefaultModeConf(String vmLiveDefaultModeConf) {
        this.setVmLiveDefaultModeConf(vmLiveDefaultModeConf);
        return this;
    }

    public void setVmLiveDefaultModeConf(String vmLiveDefaultModeConf) {
        this.vmLiveDefaultModeConf = vmLiveDefaultModeConf;
    }

    public String getVmLiveDefaultTimerangeConf() {
        return this.vmLiveDefaultTimerangeConf;
    }

    public ConfigSetting vmLiveDefaultTimerangeConf(String vmLiveDefaultTimerangeConf) {
        this.setVmLiveDefaultTimerangeConf(vmLiveDefaultTimerangeConf);
        return this;
    }

    public void setVmLiveDefaultTimerangeConf(String vmLiveDefaultTimerangeConf) {
        this.vmLiveDefaultTimerangeConf = vmLiveDefaultTimerangeConf;
    }

    public Integer getVmLivePositionCycleConf() {
        return this.vmLivePositionCycleConf;
    }

    public ConfigSetting vmLivePositionCycleConf(Integer vmLivePositionCycleConf) {
        this.setVmLivePositionCycleConf(vmLivePositionCycleConf);
        return this;
    }

    public void setVmLivePositionCycleConf(Integer vmLivePositionCycleConf) {
        this.vmLivePositionCycleConf = vmLivePositionCycleConf;
    }

    public String getVmLivePositionCycleUnitConf() {
        return this.vmLivePositionCycleUnitConf;
    }

    public ConfigSetting vmLivePositionCycleUnitConf(String vmLivePositionCycleUnitConf) {
        this.setVmLivePositionCycleUnitConf(vmLivePositionCycleUnitConf);
        return this;
    }

    public void setVmLivePositionCycleUnitConf(String vmLivePositionCycleUnitConf) {
        this.vmLivePositionCycleUnitConf = vmLivePositionCycleUnitConf;
    }

    public Integer getVmLiveTrackingAmplitudeConf() {
        return this.vmLiveTrackingAmplitudeConf;
    }

    public ConfigSetting vmLiveTrackingAmplitudeConf(Integer vmLiveTrackingAmplitudeConf) {
        this.setVmLiveTrackingAmplitudeConf(vmLiveTrackingAmplitudeConf);
        return this;
    }

    public void setVmLiveTrackingAmplitudeConf(Integer vmLiveTrackingAmplitudeConf) {
        this.vmLiveTrackingAmplitudeConf = vmLiveTrackingAmplitudeConf;
    }

    public String getVmLiveTrackingAmplitudeUnitConf() {
        return this.vmLiveTrackingAmplitudeUnitConf;
    }

    public ConfigSetting vmLiveTrackingAmplitudeUnitConf(String vmLiveTrackingAmplitudeUnitConf) {
        this.setVmLiveTrackingAmplitudeUnitConf(vmLiveTrackingAmplitudeUnitConf);
        return this;
    }

    public void setVmLiveTrackingAmplitudeUnitConf(String vmLiveTrackingAmplitudeUnitConf) {
        this.vmLiveTrackingAmplitudeUnitConf = vmLiveTrackingAmplitudeUnitConf;
    }

    public Integer getSarSysSyncCycleConf() {
        return this.sarSysSyncCycleConf;
    }

    public ConfigSetting sarSysSyncCycleConf(Integer sarSysSyncCycleConf) {
        this.setSarSysSyncCycleConf(sarSysSyncCycleConf);
        return this;
    }

    public void setSarSysSyncCycleConf(Integer sarSysSyncCycleConf) {
        this.sarSysSyncCycleConf = sarSysSyncCycleConf;
    }

    public String getSarSysSyncCycleUnitConf() {
        return this.sarSysSyncCycleUnitConf;
    }

    public ConfigSetting sarSysSyncCycleUnitConf(String sarSysSyncCycleUnitConf) {
        this.setSarSysSyncCycleUnitConf(sarSysSyncCycleUnitConf);
        return this;
    }

    public void setSarSysSyncCycleUnitConf(String sarSysSyncCycleUnitConf) {
        this.sarSysSyncCycleUnitConf = sarSysSyncCycleUnitConf;
    }

    public String getSarSysObjectDisplayName01Conf() {
        return this.sarSysObjectDisplayName01Conf;
    }

    public ConfigSetting sarSysObjectDisplayName01Conf(String sarSysObjectDisplayName01Conf) {
        this.setSarSysObjectDisplayName01Conf(sarSysObjectDisplayName01Conf);
        return this;
    }

    public void setSarSysObjectDisplayName01Conf(String sarSysObjectDisplayName01Conf) {
        this.sarSysObjectDisplayName01Conf = sarSysObjectDisplayName01Conf;
    }

    public String getSarSysObjectDisplayName02Conf() {
        return this.sarSysObjectDisplayName02Conf;
    }

    public ConfigSetting sarSysObjectDisplayName02Conf(String sarSysObjectDisplayName02Conf) {
        this.setSarSysObjectDisplayName02Conf(sarSysObjectDisplayName02Conf);
        return this;
    }

    public void setSarSysObjectDisplayName02Conf(String sarSysObjectDisplayName02Conf) {
        this.sarSysObjectDisplayName02Conf = sarSysObjectDisplayName02Conf;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public ConfigSetting createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getCreateUid() {
        return this.createUid;
    }

    public ConfigSetting createUid(String createUid) {
        this.setCreateUid(createUid);
        return this;
    }

    public void setCreateUid(String createUid) {
        this.createUid = createUid;
    }

    public Instant getLastUpdate() {
        return this.lastUpdate;
    }

    public ConfigSetting lastUpdate(Instant lastUpdate) {
        this.setLastUpdate(lastUpdate);
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateUid() {
        return this.lastUpdateUid;
    }

    public ConfigSetting lastUpdateUid(String lastUpdateUid) {
        this.setLastUpdateUid(lastUpdateUid);
        return this;
    }

    public void setLastUpdateUid(String lastUpdateUid) {
        this.lastUpdateUid = lastUpdateUid;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigSetting)) {
            return false;
        }
        return id != null && id.equals(((ConfigSetting) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConfigSetting{" +
            "id=" + getId() +
            ", sourceType='" + getSourceType() + "'" +
            ", mcUserId='" + getMcUserId() + "'" +
            ", tmlUserId='" + getTmlUserId() + "'" +
            ", vmSysDefaultModeConf='" + getVmSysDefaultModeConf() + "'" +
            ", vmSysSyncCycleConf=" + getVmSysSyncCycleConf() +
            ", vmSysSyncCycleUnitConf='" + getVmSysSyncCycleUnitConf() + "'" +
            ", vmSysTargetDisplayNameConf='" + getVmSysTargetDisplayNameConf() + "'" +
            ", vmLiveDefaultModeConf='" + getVmLiveDefaultModeConf() + "'" +
            ", vmLiveDefaultTimerangeConf='" + getVmLiveDefaultTimerangeConf() + "'" +
            ", vmLivePositionCycleConf=" + getVmLivePositionCycleConf() +
            ", vmLivePositionCycleUnitConf='" + getVmLivePositionCycleUnitConf() + "'" +
            ", vmLiveTrackingAmplitudeConf=" + getVmLiveTrackingAmplitudeConf() +
            ", vmLiveTrackingAmplitudeUnitConf='" + getVmLiveTrackingAmplitudeUnitConf() + "'" +
            ", sarSysSyncCycleConf=" + getSarSysSyncCycleConf() +
            ", sarSysSyncCycleUnitConf='" + getSarSysSyncCycleUnitConf() + "'" +
            ", sarSysObjectDisplayName01Conf='" + getSarSysObjectDisplayName01Conf() + "'" +
            ", sarSysObjectDisplayName02Conf='" + getSarSysObjectDisplayName02Conf() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", createUid='" + getCreateUid() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", lastUpdateUid='" + getLastUpdateUid() + "'" +
            "}";
    }
}
