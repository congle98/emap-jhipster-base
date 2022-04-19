package com.emap.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A WarningRule.
 */
@Entity
@Table(name = "warning_rule")
public class WarningRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Size(max = 15)
    @Column(name = "mc_user_id", length = 15, nullable = false)
    private String mcUserId;

    @NotNull
    @Column(name = "delay_check", nullable = false)
    private Integer delayCheck;

    @NotNull
    @Size(max = 15)
    @Column(name = "delay_check_unit", length = 15, nullable = false)
    private String delayCheckUnit;

    @NotNull
    @Size(max = 20)
    @Column(name = "condition_type", length = 20, nullable = false)
    private String conditionType;

    @Size(max = 15)
    @Column(name = "include_mc_campaign_id", length = 15)
    private String includeMcCampaignId;

    @Size(max = 15)
    @Column(name = "include_mc_target_id", length = 15)
    private String includeMcTargetId;

    @NotNull
    @Column(name = "warning_distance", nullable = false)
    private Integer warningDistance;

    @NotNull
    @Column(name = "show_warning_circle", nullable = false)
    private Boolean showWarningCircle;

    @NotNull
    @Column(name = "show_warning_message", nullable = false)
    private Boolean showWarningMessage;

    @Size(max = 255)
    @Column(name = "warning_message", length = 255)
    private String warningMessage;

    @NotNull
    @Column(name = "send_warning_message_to_mc", nullable = false)
    private Boolean sendWarningMessageToMc;

    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status;

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

    public WarningRule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public WarningRule name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMcUserId() {
        return this.mcUserId;
    }

    public WarningRule mcUserId(String mcUserId) {
        this.setMcUserId(mcUserId);
        return this;
    }

    public void setMcUserId(String mcUserId) {
        this.mcUserId = mcUserId;
    }

    public Integer getDelayCheck() {
        return this.delayCheck;
    }

    public WarningRule delayCheck(Integer delayCheck) {
        this.setDelayCheck(delayCheck);
        return this;
    }

    public void setDelayCheck(Integer delayCheck) {
        this.delayCheck = delayCheck;
    }

    public String getDelayCheckUnit() {
        return this.delayCheckUnit;
    }

    public WarningRule delayCheckUnit(String delayCheckUnit) {
        this.setDelayCheckUnit(delayCheckUnit);
        return this;
    }

    public void setDelayCheckUnit(String delayCheckUnit) {
        this.delayCheckUnit = delayCheckUnit;
    }

    public String getConditionType() {
        return this.conditionType;
    }

    public WarningRule conditionType(String conditionType) {
        this.setConditionType(conditionType);
        return this;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public String getIncludeMcCampaignId() {
        return this.includeMcCampaignId;
    }

    public WarningRule includeMcCampaignId(String includeMcCampaignId) {
        this.setIncludeMcCampaignId(includeMcCampaignId);
        return this;
    }

    public void setIncludeMcCampaignId(String includeMcCampaignId) {
        this.includeMcCampaignId = includeMcCampaignId;
    }

    public String getIncludeMcTargetId() {
        return this.includeMcTargetId;
    }

    public WarningRule includeMcTargetId(String includeMcTargetId) {
        this.setIncludeMcTargetId(includeMcTargetId);
        return this;
    }

    public void setIncludeMcTargetId(String includeMcTargetId) {
        this.includeMcTargetId = includeMcTargetId;
    }

    public Integer getWarningDistance() {
        return this.warningDistance;
    }

    public WarningRule warningDistance(Integer warningDistance) {
        this.setWarningDistance(warningDistance);
        return this;
    }

    public void setWarningDistance(Integer warningDistance) {
        this.warningDistance = warningDistance;
    }

    public Boolean getShowWarningCircle() {
        return this.showWarningCircle;
    }

    public WarningRule showWarningCircle(Boolean showWarningCircle) {
        this.setShowWarningCircle(showWarningCircle);
        return this;
    }

    public void setShowWarningCircle(Boolean showWarningCircle) {
        this.showWarningCircle = showWarningCircle;
    }

    public Boolean getShowWarningMessage() {
        return this.showWarningMessage;
    }

    public WarningRule showWarningMessage(Boolean showWarningMessage) {
        this.setShowWarningMessage(showWarningMessage);
        return this;
    }

    public void setShowWarningMessage(Boolean showWarningMessage) {
        this.showWarningMessage = showWarningMessage;
    }

    public String getWarningMessage() {
        return this.warningMessage;
    }

    public WarningRule warningMessage(String warningMessage) {
        this.setWarningMessage(warningMessage);
        return this;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public Boolean getSendWarningMessageToMc() {
        return this.sendWarningMessageToMc;
    }

    public WarningRule sendWarningMessageToMc(Boolean sendWarningMessageToMc) {
        this.setSendWarningMessageToMc(sendWarningMessageToMc);
        return this;
    }

    public void setSendWarningMessageToMc(Boolean sendWarningMessageToMc) {
        this.sendWarningMessageToMc = sendWarningMessageToMc;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public WarningRule status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public WarningRule createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getCreateUid() {
        return this.createUid;
    }

    public WarningRule createUid(String createUid) {
        this.setCreateUid(createUid);
        return this;
    }

    public void setCreateUid(String createUid) {
        this.createUid = createUid;
    }

    public Instant getLastUpdate() {
        return this.lastUpdate;
    }

    public WarningRule lastUpdate(Instant lastUpdate) {
        this.setLastUpdate(lastUpdate);
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateUid() {
        return this.lastUpdateUid;
    }

    public WarningRule lastUpdateUid(String lastUpdateUid) {
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
        if (!(o instanceof WarningRule)) {
            return false;
        }
        return id != null && id.equals(((WarningRule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WarningRule{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", mcUserId='" + getMcUserId() + "'" +
            ", delayCheck=" + getDelayCheck() +
            ", delayCheckUnit='" + getDelayCheckUnit() + "'" +
            ", conditionType='" + getConditionType() + "'" +
            ", includeMcCampaignId='" + getIncludeMcCampaignId() + "'" +
            ", includeMcTargetId='" + getIncludeMcTargetId() + "'" +
            ", warningDistance=" + getWarningDistance() +
            ", showWarningCircle='" + getShowWarningCircle() + "'" +
            ", showWarningMessage='" + getShowWarningMessage() + "'" +
            ", warningMessage='" + getWarningMessage() + "'" +
            ", sendWarningMessageToMc='" + getSendWarningMessageToMc() + "'" +
            ", status='" + getStatus() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", createUid='" + getCreateUid() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", lastUpdateUid='" + getLastUpdateUid() + "'" +
            "}";
    }
}
