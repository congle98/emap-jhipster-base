package com.emap.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Coordinates.
 */
@Entity
@Table(name = "coordinates")
public class Coordinates implements Serializable {

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

    @Size(max = 15)
    @Column(name = "lat", length = 15)
    private String lat;

    @Size(max = 15)
    @Column(name = "lng", length = 15)
    private String lng;

    @Column(name = "radius")
    private Double radius;

    @Column(name = "open_angle")
    private Integer openAngle;

    @Column(name = "directional_angle")
    private Integer directionalAngle;

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

    public Coordinates id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceType() {
        return this.sourceType;
    }

    public Coordinates sourceType(String sourceType) {
        this.setSourceType(sourceType);
        return this;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getMcCampaingnId() {
        return this.mcCampaingnId;
    }

    public Coordinates mcCampaingnId(String mcCampaingnId) {
        this.setMcCampaingnId(mcCampaingnId);
        return this;
    }

    public void setMcCampaingnId(String mcCampaingnId) {
        this.mcCampaingnId = mcCampaingnId;
    }

    public String getTmlCampaignId() {
        return this.tmlCampaignId;
    }

    public Coordinates tmlCampaignId(String tmlCampaignId) {
        this.setTmlCampaignId(tmlCampaignId);
        return this;
    }

    public void setTmlCampaignId(String tmlCampaignId) {
        this.tmlCampaignId = tmlCampaignId;
    }

    public String getLat() {
        return this.lat;
    }

    public Coordinates lat(String lat) {
        this.setLat(lat);
        return this;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return this.lng;
    }

    public Coordinates lng(String lng) {
        this.setLng(lng);
        return this;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Double getRadius() {
        return this.radius;
    }

    public Coordinates radius(Double radius) {
        this.setRadius(radius);
        return this;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Integer getOpenAngle() {
        return this.openAngle;
    }

    public Coordinates openAngle(Integer openAngle) {
        this.setOpenAngle(openAngle);
        return this;
    }

    public void setOpenAngle(Integer openAngle) {
        this.openAngle = openAngle;
    }

    public Integer getDirectionalAngle() {
        return this.directionalAngle;
    }

    public Coordinates directionalAngle(Integer directionalAngle) {
        this.setDirectionalAngle(directionalAngle);
        return this;
    }

    public void setDirectionalAngle(Integer directionalAngle) {
        this.directionalAngle = directionalAngle;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public Coordinates createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getCreateUid() {
        return this.createUid;
    }

    public Coordinates createUid(String createUid) {
        this.setCreateUid(createUid);
        return this;
    }

    public void setCreateUid(String createUid) {
        this.createUid = createUid;
    }

    public Instant getLastUpdate() {
        return this.lastUpdate;
    }

    public Coordinates lastUpdate(Instant lastUpdate) {
        this.setLastUpdate(lastUpdate);
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateUid() {
        return this.lastUpdateUid;
    }

    public Coordinates lastUpdateUid(String lastUpdateUid) {
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
        if (!(o instanceof Coordinates)) {
            return false;
        }
        return id != null && id.equals(((Coordinates) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Coordinates{" +
            "id=" + getId() +
            ", sourceType='" + getSourceType() + "'" +
            ", mcCampaingnId='" + getMcCampaingnId() + "'" +
            ", tmlCampaignId='" + getTmlCampaignId() + "'" +
            ", lat='" + getLat() + "'" +
            ", lng='" + getLng() + "'" +
            ", radius=" + getRadius() +
            ", openAngle=" + getOpenAngle() +
            ", directionalAngle=" + getDirectionalAngle() +
            ", createDate='" + getCreateDate() + "'" +
            ", createUid='" + getCreateUid() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", lastUpdateUid='" + getLastUpdateUid() + "'" +
            "}";
    }
}
