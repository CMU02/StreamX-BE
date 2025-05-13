package a4.streamx_be.marketplace.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarketplaceImageResponse {
    private Long id;
    private String imageUrl;

    public MarketplaceImageResponse(Long id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }
}