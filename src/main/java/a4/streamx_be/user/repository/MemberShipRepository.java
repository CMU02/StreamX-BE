package a4.streamx_be.user.repository;

import a4.streamx_be.user.domain.entity.MemberShip;
import a4.streamx_be.user.domain.model.MemberShipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberShipRepository extends JpaRepository<MemberShip, Long> {
    Optional<MemberShip> findByMemberShipType(MemberShipType memberShipType);
}
