package a4.streamx_be.user.repository;

import a4.streamx_be.user.domain.entity.UserMemberShipHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberShipHistoryRepository extends JpaRepository<UserMemberShipHistory, Long> {
}
