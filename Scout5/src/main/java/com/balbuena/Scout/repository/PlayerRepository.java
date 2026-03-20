package com.balbuena.Scout.repository;

import com.balbuena.Scout.model.Player;
import com.balbuena.Scout.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByAvailableTrue();
    List<Player> findByAuctionPlayerTrue();
    List<Player> findByAuctionPlayerFalseAndAvailableTrue();
    List<Player> findByPresidentId(Long presidentId);
    List<Player> findByAvailableTrueAndPosition(Position position);
}