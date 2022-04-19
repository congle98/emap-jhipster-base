package com.emap.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A WarningMessage.
 */
@Entity
@Table(name = "warning_message")
public class WarningMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 15)
    @Column(name = "mc_user_id", length = 15, nullable = false)
    private String mcUserId;

    @NotNull
    @Column(name = "warning_distance", nullable = false)
    private String warningDistance;

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

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private WarningRule warningRule;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WarningMessage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMcUserId() {
        return this.mcUserId;
    }

    public WarningMessage mcUserId(String mcUserId) {
        this.setMcUserId(mcUserId);
        return this;
    }

    public void setMcUserId(String mcUserId) {
        this.mcUserId = mcUserId;
    }

    public String getWarningDistance() {
        return this.warningDistance;
    }

    public WarningMessage warningDistance(String warningDistance) {
        this.setWarningDistance(warningDistance);
        return this;
    }

    public void setWarningDistance(String warningDistance) {
        this.warningDistance = warningDistance;
    }

    public Boolean getShowWarningCircle() {
        return this.showWarningCircle;
    }

    public WarningMessage showWarningCircle(Boolean showWarningCircle) {
        this.setShowWarningCircle(showWarningCircle);
        return this;
    }

    public void setShowWarningCircle(Boolean showWarningCircle) {
        this.showWarningCircle = showWarningCircle;
    }

    public Boolean getShowWarningMessage() {
        return this.showWarningMessage;
    }

    public WarningMessage showWarningMessage(Boolean showWarningMessage) {
        this.setShowWarningMessage(showWarningMessage);
        return this;
    }

    public void setShowWarningMessage(Boolean showWarningMessage) {
        this.showWarningMessage = showWarningMessage;
    }

    public String getWarningMessage() {
        return this.warningMessage;
    }

    public WarningMessage warningMessage(String warningMessage) {
        this.setWarningMessage(warningMessage);
        return this;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public Boolean getSendWarningMessageToMc() {
        return this.sendWarningMessageToMc;
    }

    public WarningMessage sendWarningMessageToMc(Boolean sendWarningMessageToMc) {
        this.setSendWarningMessageToMc(sendWarningMessageToMc);
        return this;
    }

    public void setSendWarningMessageToMc(Boolean sendWarningMessageToMc) {
        this.sendWarningMessageToMc = sendWarningMessageToMc;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public WarningMessage createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getCreateUid() {
        return this.createUid;
    }

    public WarningMessage createUid(String createUid) {
        this.setCreateUid(createUid);
        return this;
    }

    public void setCreateUid(String createUid) {
        this.createUid = createUid;
    }

    public Instant getLastUpdate() {
        return this.lastUpdate;
    }

    public WarningMessage lastUpdate(Instant lastUpdate) {
        this.setLastUpdate(lastUpdate);
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateUid() {
        return this.lastUpdateUid;
    }

    public WarningMessage lastUpdateUid(String lastUpdateUid) {
        this.setLastUpdateUid(lastUpdateUid);
        return this;
    }

    public void setLastUpdateUid(String lastUpdateUid) {
        this.lastUpdateUid = lastUpdateUid;
    }

    public WarningRule getWarningRule() {
        return this.warningRule;
    }

    public void setWarningRule(WarningRule warningRule) {
        this.warningRule = warningRule;
    }

    public WarningMessage warningRule(WarningRule warningRule) {
        this.setWarningRule(warningRule);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WarningMessage)) {
            return false;
        }
        return id != null && id.equals(((WarningMessage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WarningMessage{" +
            "id=" + getId() +
            ", mcUserId='" + getMcUserId() + "'" +
            ", warningDistance='" + getWarningDistance() + "'" +
            ", showWarningCircle='" + getShowWarningCircle() + "'" +
            ", showWarningMessage='" + getShowWarningMessage() + "'" +
            ", warningMessage='" + getWarningMessage() + "'" +
            ", sendWarningMessageToMc='" + getSendWarningMessageToMc() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", createUid='" + getCreateUid() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", lastUpdateUid='" + getLastUpdateUid() + "'" +
            "}";
    }
}
