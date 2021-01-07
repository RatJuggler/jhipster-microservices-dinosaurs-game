package com.rj.dinosaurs.game.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A HighScore.
 */
@Entity
@Table(name = "high_score")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HighScore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "score", nullable = false)
    private Integer score;

    @NotNull
    @Column(name = "achieved_dt", nullable = false)
    private Instant achievedDt;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Player player;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Level level;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public HighScore score(Integer score) {
        this.score = score;
        return this;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Instant getAchievedDt() {
        return achievedDt;
    }

    public HighScore achievedDt(Instant achievedDt) {
        this.achievedDt = achievedDt;
        return this;
    }

    public void setAchievedDt(Instant achievedDt) {
        this.achievedDt = achievedDt;
    }

    public Player getPlayer() {
        return player;
    }

    public HighScore player(Player player) {
        this.player = player;
        return this;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Level getLevel() {
        return level;
    }

    public HighScore level(Level level) {
        this.level = level;
        return this;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HighScore)) {
            return false;
        }
        return id != null && id.equals(((HighScore) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HighScore{" +
            "id=" + getId() +
            ", score=" + getScore() +
            ", achievedDt='" + getAchievedDt() + "'" +
            "}";
    }
}
