package a4.streamx_be.user.domain.dto.response;

import a4.streamx_be.user.domain.entity.MemberShip;
import a4.streamx_be.user.domain.model.MemberShipType;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberShipResponse(
        @JsonProperty("type") MemberShipType memberShip,
        Long weeklyChatLimit,
        Long WeeklyTtsLimit
) {
    public MemberShipResponse(MemberShip memberShip) {
        this(memberShip.getMemberShipType(), memberShip.getWeeklyChatLimit(), memberShip.getWeeklyTtsLimit());
    }
}
