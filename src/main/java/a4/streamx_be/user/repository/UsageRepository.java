package a4.streamx_be.user.repository;

import a4.streamx_be.user.domain.entity.UserUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsageRepository extends JpaRepository<UserUsage, Long> {
}
