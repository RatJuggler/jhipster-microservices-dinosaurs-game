package com.rj.dinosaurs.game.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Level.
 */
@Entity
@Table(name = "level")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Level implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @NotNull
    @Min(value = 0)
    @Max(value = 999)
    @Column(name = "jhi_order", nullable = false)
    private Integer order;

    
    @Lob
    @Column(name = "definition", nullable = false)
    private String definition;

    @NotNull
    @Column(name = "created_dt", nullable = false)
    private Instant createdDt;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Level name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public Level order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getDefinition() {
        return definition;
    }

    public Level definition(String definition) {
        this.definition = definition;
        return this;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Instant getCreatedDt() {
        return createdDt;
    }

    public Level createdDt(Instant createdDt) {
        this.createdDt = createdDt;
        return this;
    }

    public void setCreatedDt(Instant createdDt) {
        this.createdDt = createdDt;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Level)) {
            return false;
        }
        return id != null && id.equals(((Level) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Level{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", order=" + getOrder() +
            ", definition='" + getDefinition() + "'" +
            ", createdDt='" + getCreatedDt() + "'" +
            "}";
    }
}
