package a4.streamx_be.marketplace.repository;

import a4.streamx_be.marketplace.entity.Marketplace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketplaceRepository extends JpaRepository<Marketplace, Long> {
}
