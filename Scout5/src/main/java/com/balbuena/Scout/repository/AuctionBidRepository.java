package com.balbuena.Scout.repository;

import java.util.List;
import java.util.Optional;
import com.balbuena.Scout.model.AuctionBid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionBidRepository extends JpaRepository<AuctionBid, Long> {
    List<AuctionBid> findByPlayerIdOrderByBidAmountDesc(Long playerId);
    Optional<AuctionBid> findTopByPlayerIdOrderByBidAmountDesc(Long playerId);
    List<AuctionBid> findByPresidentId(Long presidentId);
}