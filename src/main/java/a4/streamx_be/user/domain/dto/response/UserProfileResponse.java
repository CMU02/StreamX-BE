package a4.streamx_be.user.domain.dto.response;

import a4.streamx_be.user.domain.entity.User;

public record UserProfileResponse(
        String displayName,
        String email,
        String photoUrl,
        MemberShipResponse membership
) {
    public UserProfileResponse(User user) {
        this(user.getDisplayName(), user.getEmail(), user.getPhotoUrl(), new MemberShipResponse(user.getMemberShip()));
    }
}
