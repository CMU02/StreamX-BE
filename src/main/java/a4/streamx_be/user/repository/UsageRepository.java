package a4.streamx_be.user.repository;

import a4.streamx_be.user.domain.entity.User;
import a4.streamx_be.user.domain.entity.UserUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface UsageRepository extends JpaRepository<UserUsage, Long> {
    Optional<UserUsage> findByUserAndUsageTime(User user, Instant usageTime);
}
