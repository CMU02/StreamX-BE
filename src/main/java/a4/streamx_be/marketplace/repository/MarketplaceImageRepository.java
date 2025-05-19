package a4.streamx_be.marketplace.repository;

import a4.streamx_be.marketplace.entity.MarketplaceImage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MarketplaceImageRepository extends JpaRepository<MarketplaceImage, Long> {
}