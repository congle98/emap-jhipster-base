package com.emap.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A CoordinatesDetails.
 */
@Entity
@Table(name = "coordinates_details")
public class CoordinatesDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "signal_connection_strength")
    private Integer signalConnectionStrength;

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
    private Coordinates coordinate;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Target object;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CoordinatesDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSignalConnectionStrength() {
        return this.signalConnectionStrength;
    }

    public CoordinatesDetails signalConnectionStrength(Integer signalConnectionStrength) {
        this.setSignalConnectionStrength(signalConnectionStrength);
        return this;
    }

    public void setSignalConnectionStrength(Integer signalConnectionStrength) {
        this.signalConnectionStrength = signalConnectionStrength;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public CoordinatesDetails createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getCreateUid() {
        return this.createUid;
    }

    public CoordinatesDetails createUid(String createUid) {
        this.setCreateUid(createUid);
        return this;
    }

    public void setCreateUid(String createUid) {
        this.createUid = createUid;
    }

    public Instant getLastUpdate() {
        return this.lastUpdate;
    }

    public CoordinatesDetails lastUpdate(Instant lastUpdate) {
        this.setLastUpdate(lastUpdate);
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateUid() {
        return this.lastUpdateUid;
    }

    public CoordinatesDetails lastUpdateUid(String lastUpdateUid) {
        this.setLastUpdateUid(lastUpdateUid);
        return this;
    }

    public void setLastUpdateUid(String lastUpdateUid) {
        this.lastUpdateUid = lastUpdateUid;
    }

    public Coordinates getCoordinate() {
        return this.coordinate;
    }

    public void setCoordinate(Coordinates coordinates) {
        this.coordinate = coordinates;
    }

    public CoordinatesDetails coordinate(Coordinates coordinates) {
        this.setCoordinate(coordinates);
        return this;
    }

    public Target getObject() {
        return this.object;
    }

    public void setObject(Target target) {
        this.object = target;
    }

    public CoordinatesDetails object(Target target) {
        this.setObject(target);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CoordinatesDetails)) {
            return false;
        }
        return id != null && id.equals(((CoordinatesDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoordinatesDetails{" +
            "id=" + getId() +
            ", signalConnectionStrength=" + getSignalConnectionStrength() +
            ", createDate='" + getCreateDate() + "'" +
            ", createUid='" + getCreateUid() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", lastUpdateUid='" + getLastUpdateUid() + "'" +
            "}";
    }
}
