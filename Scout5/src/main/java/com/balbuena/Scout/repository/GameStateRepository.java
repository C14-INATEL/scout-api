package com.balbuena.Scout.repository;

import com.balbuena.Scout.model.GameState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameStateRepository extends JpaRepository<GameState, Long> {

}
