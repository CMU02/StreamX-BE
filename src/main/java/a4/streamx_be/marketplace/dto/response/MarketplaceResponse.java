package a4.streamx_be.marketplace.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MarketplaceResponse {

    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MarketplaceResponse() {
    }

    public MarketplaceResponse(Long id, String title, String description, BigDecimal price, Long userId,
                               LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}