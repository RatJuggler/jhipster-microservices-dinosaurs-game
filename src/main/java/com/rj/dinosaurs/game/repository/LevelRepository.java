package com.rj.dinosaurs.game.repository;

import com.rj.dinosaurs.game.domain.Level;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Level entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {

}
