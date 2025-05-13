package a4.streamx_be.marketplace.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class MarketplaceImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private Long marketplaceId;

    private Integer orderIndex;

    private LocalDateTime createdAt = LocalDateTime.now();
}