package com.qualica.liquibasejunit.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by katlego on 1/7/14.
 */
@Entity
@Table(name = "PARTY")
@TableGenerator(name = "GEN_SEQ_PARTY",
        table = "SEQUENCES",
        pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE",
        pkColumnValue = "SEQ_PARTY",
        initialValue = 1,
        allocationSize = 10)
@NamedQueries(
        @NamedQuery(
                name = "party.findByModifiedBy",
                query = "SELECT p FROM Party p WHERE p.modifiedBy = :modifiedBy"
        )
)
public class Party implements IDomainEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "GEN_SEQ_PARTY")
    private Long id;

    @Column(name = "LAST_UPDATED")
    private Date lastUpdated;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @Column(name = "VERSION")
    private String version;

    protected Party() {}

    public Party(Date lastUpdated, String modifiedBy, String version) {
        this.lastUpdated = lastUpdated;
        this.modifiedBy = modifiedBy;
        this.version = version;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Party)) {
            return false;
        }

        Party party = (Party) o;

        if (getId() != null ? !getId().equals(party.getId()) : party.getId() != null) {
            return false;
        }

        if (lastUpdated != null ? !lastUpdated.equals(party.getLastUpdated()) : party.getLastUpdated() != null) {
            return false;
        }

        if (modifiedBy != null ? !modifiedBy.equals(party.getModifiedBy()) : party.getModifiedBy() != null) {
            return false;
        }

        if (version != null ? !version.equals(party.getVersion()) : party.getVersion() != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (lastUpdated != null ? lastUpdated.hashCode() : 0);
        result = 31 * result + (modifiedBy != null ? modifiedBy.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "Party[id=%d, modifiedBy='%s', version='%s', lastUpdated='%1$tm %1$te,%1$tY']",
                id, modifiedBy, version, lastUpdated);
    }
}
