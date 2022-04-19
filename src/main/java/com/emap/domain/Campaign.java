package com.emap.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Campaign.
 */
@Entity
@Table(name = "campaign")
public class Campaign implements Serializable {

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
    @Column(name = "mc_campaingn_id", length = 15)
    private String mcCampaingnId;

    @Size(max = 15)
    @Column(name = "tml_campaign_id", length = 15)
    private String tmlCampaignId;

    @Size(max = 225)
    @Column(name = "icon", length = 225)
    private String icon;

    @Size(max = 20)
    @Column(name = "color", length = 20)
    private String color;

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

    public Campaign id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceType() {
        return this.sourceType;
    }

    public Campaign sourceType(String sourceType) {
        this.setSourceType(sourceType);
        return this;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getMcCampaingnId() {
        return this.mcCampaingnId;
    }

    public Campaign mcCampaingnId(String mcCampaingnId) {
        this.setMcCampaingnId(mcCampaingnId);
        return this;
    }

    public void setMcCampaingnId(String mcCampaingnId) {
        this.mcCampaingnId = mcCampaingnId;
    }

    public String getTmlCampaignId() {
        return this.tmlCampaignId;
    }

    public Campaign tmlCampaignId(String tmlCampaignId) {
        this.setTmlCampaignId(tmlCampaignId);
        return this;
    }

    public void setTmlCampaignId(String tmlCampaignId) {
        this.tmlCampaignId = tmlCampaignId;
    }

    public String getIcon() {
        return this.icon;
    }

    public Campaign icon(String icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return this.color;
    }

    public Campaign color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public Campaign createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getCreateUid() {
        return this.createUid;
    }

    public Campaign createUid(String createUid) {
        this.setCreateUid(createUid);
        return this;
    }

    public void setCreateUid(String createUid) {
        this.createUid = createUid;
    }

    public Instant getLastUpdate() {
        return this.lastUpdate;
    }

    public Campaign lastUpdate(Instant lastUpdate) {
        this.setLastUpdate(lastUpdate);
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateUid() {
        return this.lastUpdateUid;
    }

    public Campaign lastUpdateUid(String lastUpdateUid) {
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
        if (!(o instanceof Campaign)) {
            return false;
        }
        return id != null && id.equals(((Campaign) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Campaign{" +
            "id=" + getId() +
            ", sourceType='" + getSourceType() + "'" +
            ", mcCampaingnId='" + getMcCampaingnId() + "'" +
            ", tmlCampaignId='" + getTmlCampaignId() + "'" +
            ", icon='" + getIcon() + "'" +
            ", color='" + getColor() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", createUid='" + getCreateUid() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", lastUpdateUid='" + getLastUpdateUid() + "'" +
            "}";
    }
}
