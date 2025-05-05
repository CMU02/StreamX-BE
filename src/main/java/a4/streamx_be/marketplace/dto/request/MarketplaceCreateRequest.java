package a4.streamx_be.marketplace.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MarketplaceCreateRequest {

    private String title;
    private String description;
    private BigDecimal price;
    private Long userId;

    public MarketplaceCreateRequest() {
    }

    public MarketplaceCreateRequest(String title, String description, BigDecimal price, Long userId) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.userId = userId;
    }
}