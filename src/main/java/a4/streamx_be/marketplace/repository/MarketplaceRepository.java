package a4.streamx_be.marketplace.repository;

import a4.streamx_be.marketplace.entity.Marketplace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarketplaceRepository extends JpaRepository<Marketplace, Long> {
    List<Marketplace> findByIsDeletedFalse();

    Optional<Marketplace> findByIdAndIsDeletedFalse(Long id);
}
